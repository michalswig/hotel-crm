import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgForOf, NgIf } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  debounceTime,
  distinctUntilChanged,
  map,
  startWith,
  switchMap,
  tap
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
import { Hotel } from '../../../../../shared/models/hotel.model';
import { EventRequest } from '../../../../../shared/models/request/event-request.model';
import { EventModel } from '../../../../../shared/models/event.model';

import { CompanyService } from '../../../../../shared/services/company.service';
import { HotelService } from '../../../../../shared/services/hotel.service';
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

  // Autocomplete helper controls
  companyCtrl = new FormControl<string>('', { nonNullable: true });
  hotelCtrl   = new FormControl<string>('', { nonNullable: true });

  companyOptions: Company[] = [];
  hotelOptions: Hotel[] = [];
  allHotels: Hotel[] = [];

  loadingCompanies = false;
  loadingHotels = false;
  loadingEvent = false;

  constructor(
    private readonly fb: FormBuilder,
    private readonly eventService: EventService,
    private readonly companyService: CompanyService,
    private readonly hotelService: HotelService,
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
      eventDate: ['', Validators.required], // yyyy-MM-ddTHH:mm (datetime-local)
      participantsNumber: [0, [Validators.required, Validators.min(0)]],
      estimatedTotalGrossRevenue: [0, [Validators.required, Validators.min(0)]],
      companyId: [null, Validators.required],
      hotelId: [null, Validators.required]
    });

    // Detect edit mode
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.eventId = +id;
        this.loadEvent(this.eventId);
      } else {
        // Only prefill when adding (in edit we show real value)
        this.prefillHotelFromUser();
      }
    });

    this.setupCompanyAutocomplete();
    this.setupHotelAutocomplete();
  }

  /* ---------- LOAD/INIT HELPERS ---------- */

  private prefillHotelFromUser(): void {
    const user = this.userService.getUser();
    if (user?.hotelId) {
      this.form.patchValue({ hotelId: user.hotelId });
      // Best effort display:
      this.hotelCtrl.setValue(user.hotelName ?? `Hotel #${user.hotelId}`);
    }
  }

  private loadEvent(id: number): void {
    this.loadingEvent = true;
    this.eventService.getEventById(id).subscribe({
      next: (ev: EventModel) => {
        // Patch form values
        this.form.patchValue({
          name: ev.name,
          description: ev.description ?? '',
          type: ev.type,
          status: ev.status,
          eventDate: this.toDatetimeLocal(ev.eventDate),
          participantsNumber: ev.participantsNumber,
          estimatedTotalGrossRevenue: ev.estimatedTotalGrossRevenue,
          companyId: ev.companyId,
          hotelId: ev.hotelId
        });

        // Show labels in autocompletes
        this.companyService.getCompanyById(ev.companyId).subscribe({
          next: c => this.companyCtrl.setValue(c?.name ?? `#${ev.companyId}`),
          error: () => this.companyCtrl.setValue(`#${ev.companyId}`)
        });

        this.hotelService.getById(ev.hotelId).subscribe({
          next: h => this.hotelCtrl.setValue(h?.name ?? `#${ev.hotelId}`),
          error: () => this.hotelCtrl.setValue(`#${ev.hotelId}`)
        });
      },
      error: () => {
        this.snack.open('Failed to load event', '', { duration: 3000 });
        this.router.navigate(['/dashboard/events']);
      },
      complete: () => (this.loadingEvent = false)
    });
  }

  private setupCompanyAutocomplete(): void {
    // Initial suggestions
    this.companyService.getFilteredCompanies(0, 10, '').subscribe({
      next: page => (this.companyOptions = page.content),
      error: () => (this.companyOptions = [])
    });

    // Live search
    this.companyCtrl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => (this.loadingCompanies = true)),
        switchMap(q => this.companyService.getFilteredCompanies(0, 10, (q ?? '').trim()))
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

  private setupHotelAutocomplete(): void {
    // Load all once (you can switch to a paginated “/hotels/filter” later)
    this.loadingHotels = true;
    this.hotelService.getAll().subscribe({
      next: hotels => {
        this.allHotels = hotels ?? [];
        this.hotelOptions = this.allHotels;
        this.loadingHotels = false;

        // Local filter on user input
        this.hotelCtrl.valueChanges
          .pipe(
            startWith(this.hotelCtrl.value ?? ''),
            debounceTime(150),
            distinctUntilChanged(),
            map(q => {
              const term = (q ?? '').toString().trim().toLowerCase();
              if (!term) return this.allHotels;

              const byId = Number(term);
              return this.allHotels.filter(
                h =>
                  (!Number.isNaN(byId) && h.id === byId) ||
                  h.name?.toLowerCase().includes(term)
              );
            })
          )
          .subscribe(opts => (this.hotelOptions = opts));
      },
      error: () => {
        this.loadingHotels = false;
        this.allHotels = [];
        this.hotelOptions = [];
        this.snack.open('Failed to load hotels', '', { duration: 3000 });
      }
    });
  }

  private toDatetimeLocal(iso: string | null | undefined): string {
    if (!iso) return '';
    const d = new Date(iso);
    const p = (n: number) => n.toString().padStart(2, '0');
    const yyyy = d.getFullYear();
    const MM = p(d.getMonth() + 1);
    const dd = p(d.getDate());
    const hh = p(d.getHours());
    const mm = p(d.getMinutes());
    return `${yyyy}-${MM}-${dd}T${hh}:${mm}`;
  }

  /* ---------- GETTERS FOR TEMPLATE ERRORS ---------- */

  get nameCtrl()   { return this.form.get('name') as FormControl<string | null>; }
  get descCtrl()   { return this.form.get('description') as FormControl<string | null>; }
  get typeCtrl()   { return this.form.get('type') as FormControl<string | null>; }
  get statusCtrl() { return this.form.get('status') as FormControl<string | null>; }
  get dateCtrl()   { return this.form.get('eventDate') as FormControl<string | null>; }
  get partCtrl()   { return this.form.get('participantsNumber') as FormControl<number | null>; }
  get revCtrl()    { return this.form.get('estimatedTotalGrossRevenue') as FormControl<number | null>; }
  get companyIdCtrl() { return this.form.get('companyId') as FormControl<number | null>; }
  get hotelIdCtrl()   { return this.form.get('hotelId') as FormControl<number | null>; }

  /* ---------- AUTOCOMPLETE SELECT HANDLERS ---------- */

  selectCompany(c: Company): void {
    this.form.patchValue({ companyId: c.id });
    this.companyCtrl.setValue(c.name);
  }

  selectHotel(h: Hotel): void {
    this.form.patchValue({ hotelId: h.id });
    this.hotelCtrl.setValue(h.name);
  }

  /* ---------- SAVE (CREATE / UPDATE) ---------- */

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const uid = this.userService.getCurrentUserId();
    if (!uid) {
      this.snack.open('User not loaded yet', '', { duration: 3000 });
      return;
    }

    // Convert datetime-local -> ISO-like string "yyyy-MM-ddTHH:mm:ss"
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
      hotelId: this.hotelIdCtrl.value!,      // if you want to force user's hotel, overwrite here
      createdByUserId: uid
    };

    if (this.isEditMode) {
      this.eventService.updateEvent(this.eventId, payload).subscribe({
        next: () => {
          this.snack.open('Event updated ✔', '', { duration: 2000 });
          this.router.navigate(['/dashboard/events']);
        },
        error: () => this.snack.open('Failed to update event', '', { duration: 3000 })
      });
    } else {
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
