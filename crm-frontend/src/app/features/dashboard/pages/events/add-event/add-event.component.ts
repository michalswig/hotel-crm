import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {EventType, Router} from '@angular/router';
import {EventStatus} from '../../../../../shared/models/enums';
import {EventService} from '../../../../../shared/services/event.service';
import {UserService} from '../../../../../shared/services/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {EventRequest} from '../../../../../shared/models/request/event-request.model';
import {MatCard, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';

@Component({
  selector: 'app-add-event',
  imports: [
    MatCard,
    MatCardTitle,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
  ],
  templateUrl: './add-event.component.html',
  styleUrl: './add-event.component.scss'
})
export class AddEventComponent  implements OnInit {
  form!: FormGroup;
  types = Object.values(EventType);
  statuses = Object.values(EventStatus);

  constructor(
    private readonly fb: FormBuilder,
    private readonly eventService: EventService,
    private readonly userService: UserService,
    private readonly snack: MatSnackBar,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      type: ['', Validators.required],
      status: ['', Validators.required],
      eventDate: ['', Validators.required],
      participantsNumber: [0, [Validators.required, Validators.min(0)]],
      estimatedTotalGrossRevenue: [0, [Validators.required, Validators.min(0)]],
      companyId: ['', Validators.required],
      hotelId: ['', Validators.required],
    });
  }

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

    const payload: EventRequest = {
      ...this.form.value,
      createdByUserId: uid
    };

    this.eventService.createEvent(payload).subscribe({
      next: () => {
        this.snack.open('Event created ✔', '', { duration: 2000 });
        this.router.navigate(['/dashboard/events']);
      },
      error: () => {
        this.snack.open('Failed to create event', '', { duration: 3000 });
      }
    });
  }
}
