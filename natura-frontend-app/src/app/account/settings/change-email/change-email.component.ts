import { Component, Inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CustomErrorStateMatcher } from 'src/app/matchers/error-state.matcher';

@Component({
  selector: 'app-change-email',
  templateUrl: './change-email.component.html',
  styleUrls: ['./change-email.component.scss']
})
export class ChangeEmailComponent {

  email: FormControl;
  matcher: CustomErrorStateMatcher;

  constructor(
    public dialogRef: MatDialogRef<ChangeEmailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {
    this.matcher = new CustomErrorStateMatcher();
    this.email =new FormControl(data, [Validators.required, Validators.email]);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
