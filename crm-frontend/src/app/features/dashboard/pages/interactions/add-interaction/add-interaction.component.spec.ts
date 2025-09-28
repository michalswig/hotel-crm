/// <reference types="jasmine" />
import { TestBed } from '@angular/core/testing';
import { AddInteractionComponent } from './add-interaction.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { InteractionService } from '../../../../../shared/services/interaction.service';
import { CompanyService } from '../../../../../shared/services/company.service';
import { ContactPersonService } from '../../../../../shared/services/contact-person.service';

// Simple stub models matching what component uses
interface Company { id: number; name: string; }
interface ContactPerson { id: number; firstName?: string; lastName?: string; }

class InteractionServiceMock {
  schedule = jasmine.createSpy('schedule').and.returnValue(of({ id: 123 }));
  getById = jasmine.createSpy('getById');
  update = jasmine.createSpy('update');
}

class CompanyServiceMock {
  getFilteredCompanies = jasmine.createSpy('getFilteredCompanies').and.returnValue(
    of({ content: [{ id: 1, name: 'Acme Inc.' } as Company] })
  );
  getCompanyById = jasmine.createSpy('getCompanyById').and.returnValue(of({ id: 1, name: 'Acme Inc.' } as Company));
}

class ContactPersonServiceMock {
  list = jasmine.createSpy('list').and.returnValue(
    of({ content: [{ id: 10, firstName: 'John', lastName: 'Doe' } as ContactPerson] })
  );
}

class RouterMock {
  navigate = jasmine.createSpy('navigate');
}

class SnackBarMock {
  open = jasmine.createSpy('open');
}

describe('AddInteractionComponent (create mode)', () => {
  let component: AddInteractionComponent;
  let router: RouterMock;
  let snack: SnackBarMock;
  let interactionSvc: InteractionServiceMock;

  beforeEach(async () => {
    router = new RouterMock();
    snack = new SnackBarMock();
    interactionSvc = new InteractionServiceMock();

    await TestBed.configureTestingModule({
      imports: [AddInteractionComponent],
      providers: [
        { provide: InteractionService, useValue: interactionSvc },
        { provide: CompanyService, useClass: CompanyServiceMock },
        { provide: ContactPersonService, useClass: ContactPersonServiceMock },
        { provide: Router, useValue: router },
        { provide: MatSnackBar, useValue: snack },
        {
          provide: ActivatedRoute,
          useValue: { paramMap: of(convertToParamMap({})) }, // no id => create mode
        },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(AddInteractionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit
  });

  it('should send date-only payload and navigate after successful create', () => {
    // Simulate user selecting a company
    component.selectCompany({ id: 1, name: 'Acme Inc.' } as Company);

    // Fill form values for create
    const date = '2025-10-05';
    component.form.patchValue({
      type: 'CALL' as any, // keep loose for test
      scheduledAt: date,
      notes: '  Follow up with proposal  ',
      companyId: 1,
      contactPersonId: 10,
    });

    // Trigger save
    component.save();

    // Expectations: schedule called with trimmed notes and YYYY-MM-DD date string
    expect(interactionSvc.schedule).toHaveBeenCalled();
    const payload = interactionSvc.schedule.calls.mostRecent().args[0];
    expect(payload).toEqual(jasmine.objectContaining({
      type: 'CALL' as any,
      scheduledAt: '2025-10-05',
      companyId: 1,
      contactPersonId: 10,
      notes: 'Follow up with proposal',
    }));

    // Snack shown and navigated to list
    expect(snack.open).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard/interactions']);
  });

  it('should display the selected date value in the date input', () => {
    const fixture = TestBed.createComponent(AddInteractionComponent);
    const comp = fixture.componentInstance;
    fixture.detectChanges();

    const date = '2025-12-31';
    comp.form.patchValue({ scheduledAt: date });
    fixture.detectChanges();

    const input: HTMLInputElement | null = fixture.nativeElement.querySelector('input[type="date"]');
    expect(input).withContext('date input should exist').not.toBeNull();
    expect((input as HTMLInputElement).value).toBe(date);
  });
});
