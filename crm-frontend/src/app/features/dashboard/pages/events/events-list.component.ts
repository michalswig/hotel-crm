import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';

import { Page } from '../../../../shared/models/helpers';
import { EventModel } from '../../../../shared/models/event.model';
import { EventService } from '../../../../shared/services/event.service';

import { MatButton, MatIconButton } from '@angular/material/button';
import { MatCard } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import {
  MatTable,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatCell,
  MatCellDef,
  MatRow,
  MatRowDef
} from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-events-list',
  standalone: true,
  templateUrl: './events-list.component.html',
  styleUrl: './events-list.component.scss',
  imports: [
    DatePipe,
    MatButton,
    MatCard,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable
  ]
})
export class EventsListComponent {
  events: EventModel[] = [];
  displayedColumns = ['name', 'type', 'status', 'eventDate', 'actions'];
  pageIndex = 0;
  pageSize = 5;
  totalElements = 0;

  constructor(
    private readonly eventService: EventService,
    private readonly router: Router,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getEvents(this.pageIndex, this.pageSize).subscribe({
      next: (page: Page<EventModel>) => {
        this.events = page.content;
        this.totalElements = page.totalElements;
      },
      error: (err) => console.error('Failed to load events', err)
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadEvents();
  }

  onAddEvent(): void {
    this.router.navigate(['dashboard', 'events', 'new']);
  }

  editEvent(eventId: number): void {
    this.router.navigate(['dashboard', 'events', eventId, 'edit']);
  }

  deleteEvent(eventId: number): void {
    const confirmed = confirm('Are you sure you want to delete this event?');
    if (!confirmed) return;

    this.eventService.deleteEvent(eventId).subscribe({
      next: () => {
        this.snack.open('Event deleted ✔', '', { duration: 2000 });
        this.loadEvents();
      },
      error: () => {
        this.snack.open('Failed to delete event', '', { duration: 3000 });
      }
    });
  }
}
