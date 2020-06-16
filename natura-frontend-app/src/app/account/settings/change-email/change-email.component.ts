import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CustomErrorStateMatcher } from 'src/app/matchers/error-state.matcher';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-change-email',
  templateUrl: './change-email.component.html',
  styleUrls: ['./change-email.component.scss']
})
export class ChangeEmailComponent implements OnInit {

  email: FormControl;
  matcher: CustomErrorStateMatcher;

  constructor(
    public dialogRef: MatDialogRef<ChangeEmailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {
    this.matcher = new CustomErrorStateMatcher();
    this.email =new FormControl(data, [Validators.required, Validators.email]);
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
