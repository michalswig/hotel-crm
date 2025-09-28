import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgForOf, NgIf } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatOptionModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';

import { InteractionType } from '../../../../../shared/models/enums';
import { Company } from '../../../../../shared/models/company.model';
import { ContactPerson } from '../../../../../shared/models/contact-person.model';
import { InteractionService } from '../../../../../shared/services/interaction.service';
import { CompanyService } from '../../../../../shared/services/company.service';
import { ContactPersonService } from '../../../../../shared/services/contact-person.service';
import { InteractionCreateRequest, InteractionUpdateRequest } from '../../../../../shared/models/request/interaction-requests';
import {Interaction} from '../../../../../shared/models/interaction.model';

@Component({
  selector: 'app-add-interaction',
  standalone: true,
  templateUrl: './add-interaction.component.html',
  styleUrls: ['./add-interaction.component.scss'],
  imports: [
    MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    MatAutocompleteModule, MatOptionModule, MatButtonModule,
    ReactiveFormsModule, RouterLink, NgIf, NgForOf
  ]
})
export class AddInteractionComponent implements OnInit {
  form!: FormGroup;
  isEditMode = false;
  private id!: number;

  types = Object.values(InteractionType);

  companyCtrl = new FormControl<string>('', { nonNullable: true });
  companyOptions: Company[] = [];
  contacts: ContactPerson[] = [];
  loadingCompanies = false;
  loadingContacts = false;

  constructor(
    private fb: FormBuilder,
    private svc: InteractionService,
    private companies: CompanyService,
    private contactSvc: ContactPersonService,
    private snack: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      type: ['', Validators.required],
      scheduledAt: ['', Validators.required], // date
      notes: ['',[Validators.maxLength(2000)]],
      companyId: [null, Validators.required],
      contactPersonId: [null, Validators.required]
    });

    this.route.paramMap.subscribe(p => {
      const id = p.get('id');
      if (id) { this.isEditMode = true; this.id = +id; this.load(this.id); }
    });

    this.setupCompanyAutocomplete();
  }

  private load(id: number) {
    this.svc.getById(id).subscribe({
      next: (i: Interaction) => {
        this.form.patchValue({
          type: i.type,
          scheduledAt: this.toDateStr(i.scheduledAt),
          notes: i.notes ?? '',
          companyId: i.companyId,
          contactPersonId: i.contactPersonId
        });

        this.companies.getCompanyById(i.companyId).subscribe({
          next: c => this.companyCtrl.setValue(c?.name ?? `#${i.companyId}`),
          error: () => this.companyCtrl.setValue(`#${i.companyId}`)
        });

        this.loadContacts(i.companyId);
      },
      error: () => { this.snack.open('Failed to load interaction', '', { duration: 2500 }); this.router.navigate(['/dashboard/interactions']); }
    });
  }

  private setupCompanyAutocomplete() {
    this.companies.getFilteredCompanies(0,10,'').subscribe({
      next: page => this.companyOptions = page.content,
      error: () => this.companyOptions = []
    });

    this.companyCtrl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.loadingCompanies = true),
      switchMap(q => this.companies.getFilteredCompanies(0,10,(q ?? '').trim()))
    ).subscribe({
      next: page => { this.companyOptions = page.content; this.loadingCompanies = false; },
      error: () => { this.companyOptions = []; this.loadingCompanies = false; }
    });
  }

  selectCompany(c: { id: number; name: string }) {
    this.form.patchValue({ companyId: c.id, contactPersonId: null });
    this.companyCtrl.setValue(c.name);
    this.loadContacts(c.id);
  }

  private loadContacts(companyId: number) {
    this.loadingContacts = true;
    this.contactSvc.list(companyId, 0, 100).subscribe({
      next: (p: any) => { this.contacts = p.content ?? []; this.loadingContacts = false; },
      error: () => { this.contacts = []; this.loadingContacts = false; }
    });
  }

  private toDateStr(iso?: string): string {
    if (!iso) return '';
    return iso.substring(0, 10);
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    const local = this.form.value.scheduledAt as string; // 'YYYY-MM-DD'
    const scheduled = local.substring(0, 10);

    if (this.isEditMode) {
      const payload: InteractionUpdateRequest = {
        type: this.form.value.type,
        scheduledAt: scheduled,
        contactPersonId: this.form.value.contactPersonId,
        notes: this.form.value.notes?.trim() || undefined
      };
      this.svc.update(this.id, payload).subscribe({
        next: () => { this.snack.open('Interaction updated ✔', '', { duration: 1500 }); this.router.navigate(['/dashboard/interactions']); },
        error: () => this.snack.open('Update failed', '', { duration: 2500 })
      });
    } else {
      const payload: InteractionCreateRequest = {
        type: this.form.value.type,
        scheduledAt: scheduled,
        companyId: this.form.value.companyId,
        contactPersonId: this.form.value.contactPersonId,
        notes: this.form.value.notes?.trim() || undefined
      };
      this.svc.schedule(payload).subscribe({
        next: () => { this.snack.open('Interaction scheduled ✔', '', { duration: 1500 }); this.router.navigate(['/dashboard/interactions']); },
        error: () => this.snack.open('Create failed', '', { duration: 2500 })
      });
    }
  }
}
