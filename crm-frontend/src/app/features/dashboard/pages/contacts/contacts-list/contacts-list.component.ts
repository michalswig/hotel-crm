import {Component, OnInit} from '@angular/core';
import {ContactPerson} from '../../../../../shared/models/contact-person.model';
import {ActivatedRoute, Router} from '@angular/router';
import {ContactPersonService} from '../../../../../shared/services/contact-person.service';
import {CompanyService} from '../../../../../shared/services/company.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Page} from '../../../../../shared/models/helpers';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatCard} from '@angular/material/card';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-contacts-list',
  imports: [
    MatCard,
    MatTable,
    MatHeaderCellDef,
    MatCellDef,
    MatIcon,
    MatIconButton,
    MatCell,
    MatHeaderCell,
    MatColumnDef,
    MatButton,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef,
    MatPaginator,
  ],
  templateUrl: './contacts-list.component.html',
  styleUrl: './contacts-list.component.scss'
})
export class ContactsListComponent implements OnInit {
  companyId!: number;
  companyName = '';
  primaryId?: number;

  displayedColumns = ['name','email','phone','position','primary','actions'];
  contacts: ContactPerson[] = [];
  pageIndex = 0; pageSize = 10; total = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private contactsSvc: ContactPersonService,
    private companiesSvc: CompanyService,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.companyId = +this.route.snapshot.paramMap.get('companyId')!;
    this.companiesSvc.getCompanyById(this.companyId).subscribe(c => {
      this.companyName = c.name;
      this.primaryId = c.primaryContactId ?? undefined;
    });
    this.load();
  }

  load() {
    this.contactsSvc.list(this.companyId, this.pageIndex, this.pageSize)
      .subscribe((p: Page<ContactPerson>) => {
        this.contacts = p.content; this.total = p.totalElements;
      });
  }

  onPage(e: PageEvent) { this.pageIndex = e.pageIndex; this.pageSize = e.pageSize; this.load(); }
  onAdd() { this.router.navigate(['../contacts/new'], { relativeTo: this.route }); }
  onEdit(id: number) { this.router.navigate(['../contacts', id, 'edit'], { relativeTo: this.route }); }

  onDelete(id: number) {
    if (!confirm('Delete this contact?')) return;
    this.contactsSvc.delete(this.companyId, id).subscribe({
      next: () => { if (this.primaryId === id) this.primaryId = undefined; this.load(); this.snack.open('Deleted ✔','',{duration:1500}); },
      error: () => this.snack.open('Delete failed','',{duration:2500})
    });
  }

  setPrimary(id: number) {
    this.companiesSvc.setPrimaryContact(this.companyId, id).subscribe({
      next: () => { this.primaryId = id; this.snack.open('Primary set ✔','',{duration:1500}); },
      error: () => this.snack.open('Failed to set primary','',{duration:2500})
    });
  }
}
