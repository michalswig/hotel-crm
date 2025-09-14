import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ContactPersonService} from '../../../../../shared/services/contact-person.service';
import {MatCard, MatCardTitle} from '@angular/material/card';
import {MatButton} from '@angular/material/button';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-add-contact',
  imports: [
    MatCard,
    MatCardTitle,
    MatFormField,
    MatButton,
    MatError,
    MatLabel,
    MatInput,
    ReactiveFormsModule,
    RouterLink,
    NgIf
  ],
  templateUrl: './add-contact.component.html',
  styleUrl: './add-contact.component.scss'
})
export class AddContactComponent implements OnInit {
  companyId!: number;
  form!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private contacts: ContactPersonService,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.companyId = +this.route.snapshot.paramMap.get('companyId')!;

    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      position: [''],
      email: ['', Validators.email],
      phoneNumber: ['', Validators.pattern(/^\+?\d{7,15}$/)]
    }, { validators: atLeastOne(['email', 'phoneNumber']) });
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    const v = this.form.value;
    const payload = {
      ...v,
      email: v.email?.trim() || undefined,
      phoneNumber: v.phoneNumber?.replace(/\s|-/g, '') || undefined,
      position: v.position?.trim() || undefined
    };

    this.contacts.create(this.companyId, payload).subscribe({
      next: () => {
        this.snack.open('Contact added ✔', '', { duration: 1500 });
        this.router.navigate(['/dashboard/companies', this.companyId, 'contacts']);
      },
      error: (err) => {
        const msg = (err?.error?.message || '').includes('Email already exists')
          ? 'Email already exists in this company' : 'Create failed';
        this.snack.open(msg, '', { duration: 2500 });
      }
    });
  }
}

function atLeastOne(keys: string[]): ValidatorFn {
  return (group: AbstractControl) => {
    const anyFilled = keys.some(k => {
      const ctrl = (group as FormGroup).get(k);
      const val = ctrl?.value;
      return val !== null && val !== undefined && String(val).trim() !== '';
    });
    return anyFilled ? null : { oneRequired: true };
  };
}
