import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {MatCard} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatSelect, MatOption} from '@angular/material/select';
import {MatButton} from '@angular/material/button';
import {MatSnackBar} from '@angular/material/snack-bar';
import {AdminUsersService, CreateUserRequest, UpdateUserRequest} from '../../../../../shared/services/admin-users.service';
import {HotelService} from '../../../../../shared/services/hotel.service';
import {RolesService, RoleDto} from '../../../../../shared/services/roles.service';
import {Hotel} from '../../../../../shared/models/hotel.model';

@Component({
  selector: 'app-add-user',
  standalone: true,
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss'],
  imports: [CommonModule, ReactiveFormsModule, MatCard, MatFormField, MatLabel, MatInput, MatSelect, MatOption, MatButton]
})
export class AddUserComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  id!: number;
  hotels: Hotel[] = [];
  roles: RoleDto[] = [];

  constructor(private readonly fb: FormBuilder,
              private readonly route: ActivatedRoute,
              private readonly router: Router,
              private readonly usersSvc: AdminUsersService,
              private readonly hotelSvc: HotelService,
              private readonly rolesSvc: RolesService,
              private readonly snack: MatSnackBar) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      userName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(64)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      hotelId: [null, Validators.required],
      roleId: [null, Validators.required]
    });

    this.hotelSvc.getAll().subscribe({ next: hs => this.hotels = hs });
    this.rolesSvc.list().subscribe({ next: rs => this.roles = rs });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEdit = true;
      this.id = Number(idParam);

      // In edit mode, password is not editable. Remove validators and ignore the value.
      const pwdCtrl = this.form.get('password');
      pwdCtrl?.clearValidators();
      pwdCtrl?.setValue('');
      pwdCtrl?.updateValueAndValidity();

      this.usersSvc.getById(this.id).subscribe({
        next: u => {
          this.form.patchValue({ userName: u.username, hotelId: u.hotelId, roleId: u.roleId });
        },
        error: () => this.snack.open('Failed to load user', '', { duration: 2000 })
      });
    }
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    if (this.isEdit) {
      // Build update payload without password
      const { userName, hotelId, roleId } = this.form.value;
      const payload: UpdateUserRequest = { userName, hotelId, roleId } as UpdateUserRequest;
      this.usersSvc.update(this.id, payload).subscribe({
        next: () => { this.snack.open('User updated ✔', '', { duration: 1500 }); this.router.navigate(['/dashboard','users']); },
        error: () => this.snack.open('Update failed', '', { duration: 2500 })
      });
    } else {
      const payload: CreateUserRequest = this.form.value;
      this.usersSvc.create(payload).subscribe({
        next: () => { this.snack.open('User created ✔', '', { duration: 1500 }); this.router.navigate(['/dashboard','users']); },
        error: () => this.snack.open('Create failed', '', { duration: 2500 })
      });
    }
  }

  cancel() { this.router.navigate(['/dashboard','users']); }
}
