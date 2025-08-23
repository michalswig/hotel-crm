import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {NgForOf, NgIf} from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import {debounceTime, distinctUntilChanged, map, startWith, switchMap, tap} from 'rxjs/operators';

import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatOptionModule} from '@angular/material/core';
import {MatButtonModule} from '@angular/material/button';

import {EventStatus, EventType} from '../../../../../shared/models/enums';
import {Company} from '../../../../../shared/models/company.model';
import {Hotel} from '../../../../../shared/models/hotel.model';
import {EventRequest} from '../../../../../shared/models/request/event-request.model';

import {CompanyService} from '../../../../../shared/services/company.service';
import {HotelService} from '../../../../../shared/services/hotel.service';
import {EventService} from '../../../../../shared/services/event.service';
import {UserService} from '../../../../../shared/services/user.service';

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

  types = Object.values(EventType);
  statuses = Object.values(EventStatus);

  companyCtrl = new FormControl<string>('', {nonNullable: true});
  hotelCtrl = new FormControl<string>('', {nonNullable: true});

  companyOptions: Company[] = [];
  hotelOptions: Hotel[] = [];
  allHotels: Hotel[] = [];

  loadingCompanies = false;
  loadingHotels = false;

  constructor(
    private readonly fb: FormBuilder,
    private readonly eventService: EventService,
    private readonly companyService: CompanyService,
    private readonly hotelService: HotelService,
    private readonly userService: UserService,
    private readonly snack: MatSnackBar,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(255)]],
      description: ['', [Validators.maxLength(500)]],
      type: ['', Validators.required],
      status: ['', Validators.required],
      eventDate: ['', Validators.required],
      participantsNumber: [0, [Validators.required, Validators.min(0)]],
      estimatedTotalGrossRevenue: [0, [Validators.required, Validators.min(0)]],
      companyId: [null, Validators.required],
      hotelId: [null, Validators.required]
    });

    // Prefill hotel from current user (optional)
    const user = this.userService.getUser();
    if (user?.hotelId) {
      this.form.patchValue({hotelId: user.hotelId});
      this.hotelCtrl.setValue(user.hotelName ?? `Hotel #${user.hotelId}`);
    }

    // Companies
    this.companyCtrl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => (this.loadingCompanies = true)),
      switchMap(q => this.companyService.getFilteredCompanies(0, 10, q?.trim() ?? ''))
    ).subscribe({
      next: page => {
        this.companyOptions = page.content;
        this.loadingCompanies = false;
      },
      error: () => {
        this.companyOptions = [];
        this.loadingCompanies = false;
      }
    });
    this.companyService.getFilteredCompanies(0, 10, '').subscribe({
      next: page => this.companyOptions = page.content,
      error: () => this.companyOptions = []
    });

    // ✅ Hotels: fetch ALL once, then filter locally by name or id
    this.loadingHotels = true;
    this.hotelService.getAll().subscribe({
      next: hotels => {
        this.allHotels = hotels ?? [];
        this.hotelOptions = this.allHotels; // initial
        this.loadingHotels = false;

        // local filter as user types
        this.hotelCtrl.valueChanges.pipe(
          startWith(this.hotelCtrl.value ?? ''),
          debounceTime(150),
          distinctUntilChanged(),
          map(q => {
            const term = (q ?? '').toString().trim().toLowerCase();
            if (!term) return this.allHotels;

            const id = Number(term);
            return this.allHotels.filter(h =>
              (!Number.isNaN(id) && h.id === id) ||
              (h.name?.toLowerCase().includes(term))
            );
          })
        ).subscribe(opts => this.hotelOptions = opts);
      },
      error: () => {
        this.loadingHotels = false;
        this.allHotels = [];
        this.hotelOptions = [];
        this.snack.open('Failed to load hotels', '', {duration: 3000});
      }
    });
  }

  // getters for template
  get nameCtrl() {
    return this.form.get('name') as FormControl<string | null>;
  }

  get descCtrl() {
    return this.form.get('description') as FormControl<string | null>;
  }

  get typeCtrl() {
    return this.form.get('type') as FormControl<string | null>;
  }

  get statusCtrl() {
    return this.form.get('status') as FormControl<string | null>;
  }

  get dateCtrl() {
    return this.form.get('eventDate') as FormControl<string | null>;
  }

  get partCtrl() {
    return this.form.get('participantsNumber') as FormControl<number | null>;
  }

  get revCtrl() {
    return this.form.get('estimatedTotalGrossRevenue') as FormControl<number | null>;
  }

  get companyIdCtrl() {
    return this.form.get('companyId') as FormControl<number | null>;
  }

  get hotelIdCtrl() {
    return this.form.get('hotelId') as FormControl<number | null>;
  }

  selectCompany(c: Company): void {
    this.form.patchValue({companyId: c.id});
    this.companyCtrl.setValue(c.name);
  }

  selectHotel(h: Hotel): void {
    this.form.patchValue({hotelId: h.id});
    this.hotelCtrl.setValue(h.name);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const uid = this.userService.getCurrentUserId();
    if (!uid) {
      this.snack.open('User not loaded yet', '', {duration: 3000});
      return;
    }

    const raw = this.dateCtrl.value!;
    const iso: string = new Date(raw).toISOString().slice(0, 19);

    const payload: EventRequest = {
      name: this.nameCtrl.value!,
      description: this.descCtrl.value ?? '',
      type: this.typeCtrl.value as EventType,
      status: this.statusCtrl.value as EventStatus,
      eventDate: iso,
      participantsNumber: this.partCtrl.value ?? 0,
      estimatedTotalGrossRevenue: this.revCtrl.value ?? 0,
      companyId: this.companyIdCtrl.value!,
      hotelId: this.hotelIdCtrl.value!,
      createdByUserId: uid
    };

    this.eventService.createEvent(payload).subscribe({
      next: () => {
        this.snack.open('Event created ✔', '', {duration: 2000});
        this.router.navigate(['/dashboard/events']);
      },
      error: () => this.snack.open('Failed to create event', '', {duration: 3000})
    });
  }
}
