import { Component, OnInit } from '@angular/core';
import { DatePipe, NgIf } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs';
import { Router } from '@angular/router';

import { MatCard } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import {
  MatTable, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef,
  MatCell, MatCellDef, MatRow, MatRowDef
} from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import {InteractionService} from '../../../../shared/services/interaction.service';
import {Interaction} from '../../../../shared/models/interaction.model';
import {Page} from '../../../../shared/models/helpers';

@Component({
  selector: 'app-interactions-list',
  standalone: true,
  templateUrl: './interactions-list.component.html',
  styleUrls: ['./interactions-list.component.scss'],
  imports: [
    DatePipe, ReactiveFormsModule, NgIf,
    MatCard, MatIcon, MatButton, MatIconButton, MatPaginator,
    MatTable, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef,
    MatCell, MatCellDef, MatRow, MatRowDef, MatFormField, MatLabel, MatInput
  ]
})
export class InteractionsListComponent implements OnInit {
  interactions: Interaction[] = [];
  displayedColumns = ['type', 'company', 'contact', 'scheduledAt', 'status', 'actions'];

  searchControl = new FormControl<string>('', { nonNullable: true });
  nameFilter = '';

  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  constructor(
    private readonly svc: InteractionService,
    private readonly router: Router,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(v => { this.nameFilter = (v ?? '').trim(); this.pageIndex = 0; this.load(); })
    ).subscribe();
    this.load();
  }

  load(): void {
    const filter: any = { withFollowUpOnly: false };
    const q = (this.nameFilter || '').trim();
    if (q) {
      filter.q = q;
    }
    this.svc.filter(filter, this.pageIndex, this.pageSize).subscribe({
      next: (page: Page<Interaction>) => {
        this.interactions = page.content;
        this.totalElements = page.totalElements;
      },
      error: () => this.snack.open('Error loading interactions', '', { duration: 2500 })
    });
  }

  onPageChange(e: PageEvent) { this.pageIndex = e.pageIndex; this.pageSize = e.pageSize; this.load(); }

  schedule() { this.router.navigate(['dashboard','interactions','new']); }
  edit(id: number) { this.router.navigate(['dashboard','interactions', id, 'edit']); }

  complete(id: number) {
    // open dialog below (CompleteInteractionDialogComponent)
    this.router.navigateByUrl(`/dashboard/interactions/${id}/edit`); // or use dialog immediately
  }

  remove(id: number) {
    if (!confirm('Delete this interaction?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.snack.open('Deleted ✔', '', { duration: 1500 }); this.load(); },
      error: () => this.snack.open('Delete failed', '', { duration: 2500 })
    });
  }

  statusOf(i: Interaction) {
    return i.completedAt ? 'DONE' : (new Date(i.scheduledAt) > new Date() ? 'PLANNED' : 'OVERDUE');
  }
}
