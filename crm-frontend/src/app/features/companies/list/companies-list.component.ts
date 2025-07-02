import {Component, OnInit} from '@angular/core';
import {Company} from '../../../shared/models/company.model';
import {CompanyService} from '../company.service';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatCard, MatCardTitle} from '@angular/material/card';

@Component({
  selector: 'app-companies-list',
  imports: [
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatRowDef,
    MatHeaderRow,
    MatRow,
    MatCardTitle,
    MatCard
  ],
  templateUrl: './companies-list.component.html',
  styleUrl: './companies-list.component.scss'
})
export class CompaniesListComponent implements OnInit {
  companies: Company[] = [];
  displayedColumns = ['name', 'email', 'phoneNumber'];

  constructor(private readonly companyService: CompanyService) {}

  ngOnInit(): void {
    this.companyService.getMine().subscribe({
      next: (page) => this.companies = page.content,
      error: (err) => console.error('Failed to load companies', err)
    });
  }
}
