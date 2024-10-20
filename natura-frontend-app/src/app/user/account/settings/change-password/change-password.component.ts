import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatLegacyDialogRef as MatDialogRef } from '@angular/material/legacy-dialog';
import { CrossFieldErrorMatcher } from 'src/app/core/matchers/cross-field-error.matcher';
import { matchValidator } from 'src/app/core/validators/match.validator';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  matcher: CrossFieldErrorMatcher;

  passwords: any = {};
  passwordForm: UntypedFormGroup;

  constructor(
    private formBuilder: UntypedFormBuilder,
    public dialogRef: MatDialogRef<ChangePasswordComponent>) {
    this.matcher = new CrossFieldErrorMatcher();
  }

  ngOnInit(): void {
    this.passwordForm = this.formBuilder.group({
      old: ['', Validators.required],
      new: ['', Validators.required],
      confirm: ['', Validators.required]
    }, { validator: matchValidator });
  }

  onChange() {
    const formValue = this.passwordForm.value;

    this.passwords = {
      old: formValue['old'],
      new: formValue['new']
    }

    this.dialogRef.close(this.passwords);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
