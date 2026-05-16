import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteInteractionDialogComponent } from './complete-interaction-dialog.component';

describe('CompleteInteractionDialogComponent', () => {
  let component: CompleteInteractionDialogComponent;
  let fixture: ComponentFixture<CompleteInteractionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompleteInteractionDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompleteInteractionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
