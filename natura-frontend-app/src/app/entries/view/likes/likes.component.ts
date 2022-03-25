import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from 'src/app/models/user.model';

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
