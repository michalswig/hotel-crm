import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardTitle} from '@angular/material/card';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {Industry} from '../../../../shared/models/enums';
import {CompanyService} from '../../../../shared/services/company.service';
import {UserService} from '../../../../shared/services/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router, RouterLink} from '@angular/router';
import {CompanyRequest} from '../../../../shared/models/request/company-request.model';
import {NgForOf, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatSelect} from '@angular/material/select';
import {MatOption} from '@angular/material/core';

@Component({
  selector: 'app-add-company',
  imports: [
    MatCard,
    MatCardTitle,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatError,
    MatSelect,
    MatInput,
    NgIf,
    MatOption,
    MatButton,
    RouterLink,
    NgForOf,
  ],
  templateUrl: './add-company.component.html',
  styleUrl: './add-company.component.scss'
})
export class AddCompanyComponent implements OnInit {
  form!: FormGroup;
  industries = Object.values(Industry);

  constructor(
    private readonly fb: FormBuilder,
    private readonly companyService: CompanyService,
    private readonly userService: UserService,
    private readonly snack: MatSnackBar,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name:        ['', Validators.required],
      taxId:       [''],
      industry:    ['', Validators.required],
      email:       ['', [Validators.email]],
      phoneNumber: [''],
      website:     [''],
      address:     [''],
      postalCode:  [''],
      city:        [''],
      country:     ['']
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

    const payload: CompanyRequest = {
      ...this.form.value,
      createdByUserId: uid
    } as CompanyRequest;

    this.companyService.createCompany(payload).subscribe({
      next: () => {
        this.snack.open('Company added ✔', '', { duration: 2000 });
        this.router.navigate(['/dashboard/companies']);
      },
      error: () =>
        this.snack.open('Failed to add company', '', { duration: 3000 })
    });
  }

}
