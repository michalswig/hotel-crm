import { Component, OnInit } from '@angular/core';
import {
  FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgForOf, NgIf } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  debounceTime, distinctUntilChanged, switchMap, tap
} from 'rxjs/operators';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatOptionModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';

import { EventStatus, EventType } from '../../../../../shared/models/enums';
import { Company } from '../../../../../shared/models/company.model';
import { ContactPerson } from '../../../../../shared/models/contact-person.model';
import { EventRequest } from '../../../../../shared/models/request/event-request.model';
import { EventModel } from '../../../../../shared/models/event.model';

import { CompanyService } from '../../../../../shared/services/company.service';
import { ContactPersonService } from '../../../../../shared/services/contact-person.service';
import { EventService } from '../../../../../shared/services/event.service';
import { UserService } from '../../../../../shared/services/user.service';

@Component({
  selector: 'app-add-event',
  standalone: true,
  templateUrl: './add-event.component.html',
  styleUrl: './add-event.component.scss',
  imports: [
    MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    MatAutocompleteModule, MatOptionModule, MatButtonModule,
    ReactiveFormsModule, RouterLink, NgIf, NgForOf
  ]
})
export class AddEventComponent implements OnInit {
  form!: FormGroup;

  isEditMode = false;
  private eventId!: number;

  types = Object.values(EventType);
  statuses = Object.values(EventStatus);

  // Company autocomplete
  companyCtrl = new FormControl<string>('', { nonNullable: true });
  companyOptions: Company[] = [];
  loadingCompanies = false;

  // Contacts for selected company
  contacts: ContactPerson[] = [];
  loadingContacts = false;
  originalContactId: number | undefined; // to detect clearing on update

  constructor(
    private readonly fb: FormBuilder,
    private readonly eventService: EventService,
    private readonly companyService: CompanyService,
    private readonly contactsService: ContactPersonService,
    private readonly userService: UserService,
    private readonly snack: MatSnackBar,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(255)]],
      description: ['', [Validators.maxLength(500)]],
      type: ['', Validators.required],
      status: ['', Validators.required],
      eventDate: ['', Validators.required], // yyyy-MM-ddTHH:mm
      participantsNumber: [0, [Validators.required, Validators.min(0)]],
      estimatedTotalGrossRevenue: [0, [Validators.required, Validators.min(0)]],
      companyId: [null, Validators.required],
      contactPersonId: [null] // optional; on update set to 0 to clear
    });

    // Are we editing?
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.eventId = +id;
        this.loadEvent(this.eventId);
      }
    });

    this.setupCompanyAutocomplete();
  }

  /* ---------- LOAD/INIT HELPERS ---------- */

  private loadEvent(id: number): void {
    this.eventService.getEventById(id).subscribe({
      next: (ev: EventModel) => {
        this.form.patchValue({
          name: ev.name,
          description: ev.description ?? '',
          type: ev.type,
          status: ev.status,
          eventDate: this.toDatetimeLocal(ev.eventDate),
          participantsNumber: ev.participantsNumber,
          estimatedTotalGrossRevenue: ev.estimatedTotalGrossRevenue,
          companyId: ev.companyId,
          contactPersonId: ev.contactPersonId ?? null
        });
        this.originalContactId = ev.contactPersonId ?? undefined;

        // Show company name in autocomplete input
        this.companyService.getCompanyById(ev.companyId).subscribe({
          next: c => this.companyCtrl.setValue(c?.name ?? `#${ev.companyId}`),
          error: () => this.companyCtrl.setValue(`#${ev.companyId}`)
        });

        // Load contacts for the company to populate the select
        this.loadContactsForCompany(ev.companyId);
      },
      error: () => {
        this.snack.open('Failed to load event', '', { duration: 3000 });
        this.router.navigate(['/dashboard/events']);
      }
    });
  }

  private setupCompanyAutocomplete(): void {
    // initial suggestions
    this.companyService.getFilteredCompanies(0, 10, '').subscribe({
      next: page => (this.companyOptions = page.content),
      error: () => (this.companyOptions = [])
    });

    // live search
    this.companyCtrl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => (this.loadingCompanies = true)),
        switchMap(q =>
          this.companyService.getFilteredCompanies(0, 10, (q ?? '').trim())
        )
      )
      .subscribe({
        next: page => {
          this.companyOptions = page.content;
          this.loadingCompanies = false;
        },
        error: () => {
          this.companyOptions = [];
          this.loadingCompanies = false;
        }
      });
  }

  private loadContactsForCompany(companyId: number): void {
    this.loadingContacts = true;
    // backend endpoint: GET /companies/{companyId}/contacts (paginated)
    // we just take first 100; adjust as needed
    this.contactsService.list(companyId, { page: 0, size: 100 } as any).subscribe({
      next: (p: any) => {
        this.contacts = p.content ?? [];
        this.loadingContacts = false;
      },
      error: () => {
        this.contacts = [];
        this.loadingContacts = false;
      }
    });
  }

  private toDatetimeLocal(iso: string | null | undefined): string {
    if (!iso) return '';
    const d = new Date(iso);
    const p = (n: number) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
    // seconds are optional for <input type="datetime-local">
  }

  /* ---------- GETTERS ---------- */

  get nameCtrl() { return this.form.get('name') as FormControl<string | null>; }
  get descCtrl() { return this.form.get('description') as FormControl<string | null>; }
  get typeCtrl() { return this.form.get('type') as FormControl<string | null>; }
  get statusCtrl() { return this.form.get('status') as FormControl<string | null>; }
  get dateCtrl() { return this.form.get('eventDate') as FormControl<string | null>; }
  get partCtrl() { return this.form.get('participantsNumber') as FormControl<number | null>; }
  get revCtrl() { return this.form.get('estimatedTotalGrossRevenue') as FormControl<number | null>; }
  get companyIdCtrl() { return this.form.get('companyId') as FormControl<number | null>; }
  get contactIdCtrl() { return this.form.get('contactPersonId') as FormControl<number | null>; }

  /* ---------- SELECT HANDLERS ---------- */

  selectCompany(c: Company): void {
    this.form.patchValue({ companyId: c.id, contactPersonId: null });
    this.companyCtrl.setValue(c.name);
    this.loadContactsForCompany(c.id);
  }

  clearContact(): void {
    this.contactIdCtrl.setValue(null);
  }

  /* ---------- SAVE ---------- */

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // datetime-local → ISO-like string with seconds
    const raw = this.dateCtrl.value!;
    const iso = new Date(raw).toISOString().slice(0, 19);

    const payload: EventRequest = {
      name: this.nameCtrl.value!,
      description: this.descCtrl.value ?? '',
      type: this.typeCtrl.value as EventType,
      status: this.statusCtrl.value as EventStatus,
      eventDate: iso,
      participantsNumber: this.partCtrl.value ?? 0,
      estimatedTotalGrossRevenue: this.revCtrl.value ?? 0,
      companyId: this.companyIdCtrl.value!,
      // on create: include only if set
      // on update: if cleared, send 0 to clear on backend
      contactPersonId: undefined
    };

    const selectedContact = this.contactIdCtrl.value;
    if (this.isEditMode) {
      payload.contactPersonId =
        selectedContact == null && this.originalContactId !== undefined ? 0 : selectedContact ?? undefined;

      this.eventService.updateEvent(this.eventId, payload).subscribe({
        next: () => {
          this.snack.open('Event updated ✔', '', { duration: 2000 });
          this.router.navigate(['/dashboard/events']);
        },
        error: () => this.snack.open('Failed to update event', '', { duration: 3000 })
      });
    } else {
      if (selectedContact != null) payload.contactPersonId = selectedContact;

      this.eventService.createEvent(payload).subscribe({
        next: () => {
          this.snack.open('Event created ✔', '', { duration: 2000 });
          this.router.navigate(['/dashboard/events']);
        },
        error: () => this.snack.open('Failed to create event', '', { duration: 3000 })
      });
    }
  }
}
