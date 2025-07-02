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
    MatHeaderCellDef
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
