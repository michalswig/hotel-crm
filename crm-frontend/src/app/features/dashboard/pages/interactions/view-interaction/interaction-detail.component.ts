import {Component, Inject, OnInit, Optional} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CommonModule, DatePipe, NgIf} from '@angular/common';
import {MatCard} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {InteractionService} from '../../../../../shared/services/interaction.service';
import {Interaction} from '../../../../../shared/models/interaction.model';
import {MatAnchor, MatIconButton} from '@angular/material/button';

@Component({
  selector: 'app-interaction-detail',
  standalone: true,
  imports: [CommonModule, DatePipe, NgIf, MatCard, MatIcon, RouterLink, MatAnchor, MatIconButton],
  template: `
    <mat-card class="detail-card" *ngIf="interaction as i">
      <div class="header">
        <h2>Interaction #{{ i.id }}</h2>
        <div class="actions">
          <a mat-stroked-button color="primary" [routerLink]="['/dashboard','interactions', i.id, 'edit']" (click)="close()">
            <mat-icon>edit</mat-icon>
            Edit
          </a>
          <button *ngIf="dialogRef" mat-icon-button aria-label="Close" (click)="close()" title="Close">
            <mat-icon>close</mat-icon>
          </button>
        </div>
      </div>

      <div class="grid">
        <div>
          <div class="label">Type</div>
          <div class="value">{{ i.type }}</div>
        </div>
        <div>
          <div class="label">Status</div>
          <div class="value">{{ statusOf(i) }}</div>
        </div>
        <div>
          <div class="label">Company</div>
          <div class="value">{{ i.companyName || ('#' + i.companyId) }}</div>
        </div>
        <div>
          <div class="label">Contact</div>
          <div class="value">{{ i.contactPersonName || ('#' + i.contactPersonId) }}</div>
        </div>
        <div>
          <div class="label">Scheduled</div>
          <div class="value">{{ i.scheduledAt | date:'mediumDate' }}</div>
        </div>
        <div>
          <div class="label">Completed</div>
          <div class="value">{{ i.completedAt ? (i.completedAt | date:'short') : '—' }}</div>
        </div>
        <div>
          <div class="label">Follow up</div>
          <div class="value">{{ i.followUpAt ? (i.followUpAt | date:'mediumDate') : '—' }}</div>
        </div>
      </div>

      <div class="notes" *ngIf="i.notes">
        <div class="label">Notes</div>
        <pre>{{ i.notes }}</pre>
      </div>
    </mat-card>

    <mat-card *ngIf="!interaction">
      Loading...
    </mat-card>
  `,
  styles: [`
    .detail-card { padding: 16px; }
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .header .actions { display: flex; align-items: center; gap: 8px; }
    .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 12px; }
    .label { font-size: 12px; color: #666; text-transform: uppercase; }
    .value { font-size: 14px; font-weight: 600; }
    .notes { margin-top: 16px; }
    pre { white-space: pre-wrap; background: #fafafa; padding: 12px; border-radius: 4px; }
  `]
})
export class InteractionDetailComponent implements OnInit {
  interaction?: Interaction;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly svc: InteractionService,
    @Optional() public dialogRef?: MatDialogRef<InteractionDetailComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data?: { id?: number }
  ) {}

  ngOnInit(): void {
    const dataId = this.data?.id;
    const routeId = Number(this.route.snapshot.paramMap.get('id'));
    const id = dataId ?? (Number.isFinite(routeId) && routeId > 0 ? routeId : undefined);
    if (id) {
      this.svc.getById(id).subscribe(i => this.interaction = i);
    }
  }

  close() { this.dialogRef?.close(); }

  statusOf(i: Interaction) {
    if (i.status) return i.status;
    return i.completedAt ? 'DONE' : (new Date(i.scheduledAt) > new Date() ? 'PLANNED' : 'OVERDUE');
  }
}
