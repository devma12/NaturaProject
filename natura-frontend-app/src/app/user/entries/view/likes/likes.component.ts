import { Component, Inject } from '@angular/core';
import { MatLegacyDialogRef as MatDialogRef, MAT_LEGACY_DIALOG_DATA as MAT_DIALOG_DATA } from '@angular/material/legacy-dialog';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-likes',
  templateUrl: './likes.component.html',
  styleUrls: ['./likes.component.scss']
})
export class LikesComponent {

  likes: User[] = [];

  constructor(public dialogRef: MatDialogRef<LikesComponent>,
    @Inject(MAT_DIALOG_DATA) public data: User[]) {
      this.likes = data;
     }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
