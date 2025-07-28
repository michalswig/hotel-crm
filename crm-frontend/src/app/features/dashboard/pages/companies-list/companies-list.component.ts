import {Component, OnInit} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {Company} from '../../../../shared/models/company.model';
import {CompanyService} from '../../../../shared/services/company.service';
import {MatButton, MatIconButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {Page} from '../../../../shared/models/helpers';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {debounceTime, distinctUntilChanged, tap} from 'rxjs';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {NgIf} from '@angular/common';
import {UserService} from '../../../../shared/services/user.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-companies-list',
  imports: [
    MatCard,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable,
    MatHeaderCellDef,
    MatButton,
    MatPaginator,
    MatFormField,
    MatLabel,
    ReactiveFormsModule,
    MatInput,
    MatIcon,
    NgIf,
    MatIconButton,
  ],
  templateUrl: './companies-list.component.html',
  styleUrl: './companies-list.component.scss'
})
export class CompaniesListComponent implements OnInit {
  companies: Company[] = [];
  displayedColumns = ['name', 'email', 'phoneNumber', 'actions'];

  searchControl = new FormControl<string>('', { nonNullable: true });
  nameFilter = '';

  pageIndex = 0;
  pageSize = 5;
  totalElements = 0;

  currentUserId: number | null = null;

  constructor(
    private readonly companyService: CompanyService,
    private readonly router: Router,
    private readonly userService: UserService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.userService.getCurrentUserId();

    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap((value: string) => {
        this.nameFilter = value.trim();
        this.pageIndex = 0;
        this.loadCompanies();
      })
    ).subscribe();

    this.loadCompanies();
  }

  loadCompanies(): void {
    this.companyService.getFilteredCompanies(this.pageIndex, this.pageSize, this.nameFilter).subscribe({
      next: (page: Page<Company>) => {
        this.companies = page.content;
        this.totalElements = page.totalElements;
      },
      error: (err) => console.error('Failed to load companies', err)
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadCompanies();
  }

  onAddCompany(): void {
    this.router.navigate(['dashboard', 'companies', 'new']);
  }

  onEditCompany(companyId: number): void {
    this.router.navigate(['dashboard', 'companies', companyId, 'edit']);
  }

  onDeleteCompany(companyId: number): void {
    const confirmed = confirm('Are you sure you want to delete this company?');
    if (!confirmed) return;

    this.companyService.deleteCompany(companyId).subscribe({
      next: () => {
        this.snack.open('Company deleted ✔', '', { duration: 2000 });
        this.loadCompanies();
      },
      error: () => {
        this.snack.open('Failed to delete company ', '', { duration: 3000 });
      }
    });
  }
}
