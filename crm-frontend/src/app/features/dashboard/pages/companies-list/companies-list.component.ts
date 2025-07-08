import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardTitle} from "@angular/material/card";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow, MatRowDef, MatTable
} from "@angular/material/table";
import {Company} from '../../../../shared/models/company.model';
import {CompanyService} from '../../../../shared/services/company.service';
import {MatButton} from '@angular/material/button';
import {Router} from '@angular/router';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {Page} from '../../../../shared/models/helpers';

@Component({
  selector: 'app-companies-list',
  imports: [
    MatCard,
    MatCardTitle,
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
  ],
  templateUrl: './companies-list.component.html',
  styleUrl: './companies-list.component.scss'
})
export class CompaniesListComponent implements OnInit {
  companies: Company[] = [];
  displayedColumns = ['name', 'email', 'phoneNumber'];

  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  constructor(
    private readonly companyService: CompanyService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.companyService.getMyCompanies(this.pageIndex, this.pageSize).subscribe({
      next: (page: Page<Company>) => {
        this.companies = page.content;
        this.totalElements = page.totalElements;
      },
      error: (err) => {
        console.error('❌ Failed to load companies', err);
      }
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
