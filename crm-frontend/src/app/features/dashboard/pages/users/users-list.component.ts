import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule, NgIf} from '@angular/common';
import {MatCard} from '@angular/material/card';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatTable, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatCell, MatCellDef, MatRow, MatRowDef} from '@angular/material/table';
import {MatSnackBar} from '@angular/material/snack-bar';
import {AdminUser, AdminUsersService} from '../../../../shared/services/admin-users.service';

@Component({
  selector: 'app-users-list',
  standalone: true,
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss'],
  imports: [CommonModule, NgIf, MatCard, MatButton, MatIcon, MatIconButton, MatTable, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatCell, MatCellDef, MatRow, MatRowDef]
})
export class UsersListComponent implements OnInit {
  users: AdminUser[] = [];
  displayedColumns = ['username','hotel','role','actions'];

  constructor(private readonly svc: AdminUsersService,
              private readonly router: Router,
              private readonly snack: MatSnackBar) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.svc.list().subscribe({
      next: users => this.users = users,
      error: () => this.snack.open('Failed to load users', '', { duration: 2500 })
    });
  }

  add() { this.router.navigate(['dashboard','users','new']); }
  edit(id: number) { this.router.navigate(['dashboard','users', id, 'edit']); }

  remove(id: number) {
    if (!confirm('Delete this user?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.snack.open('User deleted', '', { duration: 1500 }); this.load(); },
      error: () => this.snack.open('Delete failed', '', { duration: 2500 })
    });
  }
}
