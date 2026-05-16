import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardTitle} from '@angular/material/card';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { MatFormField, MatLabel, MatError } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import {Industry} from '../../../../shared/models/enums';
import {CompanyService} from '../../../../shared/services/company.service';
import {UserService} from '../../../../shared/services/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {CompanyRequest} from '../../../../shared/models/request/company-request.model';
import {NgForOf, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatSelect} from '@angular/material/select';
import {MatOption} from '@angular/material/core';
import {Company} from '../../../../shared/models/company.model';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmDialogComponent} from '../contacts/dialogs/confirm-dialog/confirm-dialog.component';

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
  isEditMode = false;
  companyId!: number;

  constructor(
    private readonly fb: FormBuilder,
    private readonly companyService: CompanyService,
    private readonly userService: UserService,
    private readonly snack: MatSnackBar,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      taxId: ['', [Validators.pattern(/\d{10}/)]],
      industry: ['', Validators.required],
      email: ['', Validators.email],
      phoneNumber: ['', [Validators.pattern(/\+?\d{7,15}/)]],
      website: [''],
      address: [''],
      postalCode: ['', [Validators.pattern(/\d{2}-\d{3}/)]],
      city: [''],
      country: ['']
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.companyId = +id;
        this.loadCompany();
      }
    });
  }

  loadCompany(): void {
    this.companyService.getCompanyById(this.companyId).subscribe({
      next: (company: Company) => {
        this.form.patchValue(company);
      },
      error: () => {
        this.snack.open('Failed to load company', '', {duration: 3000});
        this.router.navigate(['/dashboard/companies']);
      }
    });
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    const uid = this.userService.getCurrentUserId();
    if (!uid) { this.snack.open('User not loaded yet', '', { duration: 3000 }); return; }

    const payload: CompanyRequest = { ...this.form.value, createdByUserId: uid };

    if (this.isEditMode) {
      this.companyService.updateCompany(this.companyId, payload).subscribe({
        next: () => {
          this.snack.open('Company updated ✔', '', { duration: 2000 });
          this.router.navigate(['/dashboard/companies']);
        },
        error: () => this.snack.open('Failed to update company', '', { duration: 3000 })
      });
    } else {
      this.companyService.createCompany(payload).subscribe({
        next: (created: Company) => {
          this.snack.open('Company added ✔', '', { duration: 1200 });

          this.dialog.open(ConfirmDialogComponent, {
            data: {
              title: 'Add contact person?',
              message: 'Do you want to add a contact person now?',
              okText: 'Add contact',
              cancelText: 'Later'
            }
          }).afterClosed().subscribe(yes => {
            if (yes) {
              this.router.navigate(['/dashboard/companies', created.id, 'contacts', 'new']); // ⬅️ formularz kontaktu
            } else {
              this.router.navigate(['/dashboard/companies']);
            }
          });
        },
        error: () => this.snack.open('Failed to add company', '', { duration: 3000 })
      });
    }
  }
}
