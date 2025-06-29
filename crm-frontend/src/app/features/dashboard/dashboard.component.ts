import {Component, OnInit} from '@angular/core';
import {DatePipe, DecimalPipe, NgForOf} from '@angular/common';
import {CompanySummary, DashboardService} from '../../core/auth/dashboard.service';

@Component({
  selector: 'app-dashboard',
  imports: [
    DatePipe,
    DecimalPipe,
    NgForOf
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  companySummary: CompanySummary[] = [];

  constructor(private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.dashboardService.getCompanySummary().subscribe({
      next: (data) => this.companySummary = data,
      error: (err) => console.error('Failed to fetch company summary', err)
    });
  }
}
