import { Component, Inject } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatLegacyDialogRef as MatDialogRef, MAT_LEGACY_DIALOG_DATA as MAT_DIALOG_DATA } from '@angular/material/legacy-dialog';
import { CustomErrorStateMatcher } from 'src/app/core/matchers/error-state.matcher';

@Component({
  selector: 'app-change-email',
  templateUrl: './change-email.component.html',
  styleUrls: ['./change-email.component.scss']
})
export class ChangeEmailComponent {

  email: UntypedFormControl;
  matcher: CustomErrorStateMatcher;

  constructor(
    public dialogRef: MatDialogRef<ChangeEmailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {
    this.matcher = new CustomErrorStateMatcher();
    this.email =new UntypedFormControl(data, [Validators.required, Validators.email]);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
