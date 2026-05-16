import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { InteractionService } from '../../../../../shared/services/interaction.service';

@Component({
  selector: 'app-complete-interaction-dialog',
  standalone: true,
  templateUrl: './complete-interaction-dialog.component.html',
  styleUrls: ['./complete-interaction-dialog.component.scss'],
  imports: [
    ReactiveFormsModule,
    MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule
  ]
})
export class CompleteInteractionDialogComponent implements OnInit {
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { id: number },
    private dialogRef: MatDialogRef<CompleteInteractionDialogComponent>,
    private svc: InteractionService,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      followUpAt: ['', Validators.required],
      notes: ['', [Validators.required, Validators.maxLength(2000)]],
    });
  }


  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    const followUp = (this.form.value.followUpAt as string).trim(); // 'YYYY-MM-DDTHH:mm'

    this.svc.complete(this.data.id, {
      followUpAt: followUp,
      notes: this.form.value.notes!.trim()
    }).subscribe({
      next: (res) => {
        this.snack.open('Completed ✔', '', { duration: 1500 });
        this.dialogRef.close(res);                           // return updated Interaction to caller
      },
      error: () => this.snack.open('Complete failed', '', { duration: 2500 })
    });
  }
}
