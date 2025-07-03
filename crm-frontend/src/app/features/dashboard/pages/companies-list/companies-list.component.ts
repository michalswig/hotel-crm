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
    MatButton
  ],
  templateUrl: './companies-list.component.html',
  styleUrl: './companies-list.component.scss'
})
export class CompaniesListComponent implements OnInit {
  companies: Company[] = [];
  displayedColumns = ['name', 'email', 'phoneNumber'];

  constructor(
    private readonly companyService: CompanyService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.companyService.getMyCompanies().subscribe({
      next: (page) => this.companies = page.content,
      error: (err) => console.error('Failed to load companies', err)
    });
  }

  onAddCompany(): void {
    this.router.navigate(['dashboard', 'companies', 'new']);
  }

}
