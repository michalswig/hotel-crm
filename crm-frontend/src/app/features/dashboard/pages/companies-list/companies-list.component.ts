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
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {Page} from '../../../../shared/models/helpers';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {debounceTime, distinctUntilChanged, tap} from 'rxjs';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {NgIf} from '@angular/common';

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
  ],
  templateUrl: './companies-list.component.html',
  styleUrl: './companies-list.component.scss'
})
export class CompaniesListComponent implements OnInit {
  companies: Company[] = [];
  displayedColumns = ['name', 'email', 'phoneNumber'];

  searchControl = new FormControl<string>('', {nonNullable: true});
  nameFilter = '';

  pageIndex = 0;
  pageSize = 5;
  totalElements = 0;

  constructor(
    private readonly companyService: CompanyService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
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

}
