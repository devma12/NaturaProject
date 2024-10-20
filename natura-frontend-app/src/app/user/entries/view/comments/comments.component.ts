import { Component, Inject } from '@angular/core';
import { MatLegacyDialogRef as MatDialogRef, MAT_LEGACY_DIALOG_DATA as MAT_DIALOG_DATA } from '@angular/material/legacy-dialog';
import { Comment } from 'src/app/core/models/comment.model';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent {

  comments: Comment[] = [];
  newComment: string = '';

  constructor(public dialogRef: MatDialogRef<CommentsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Comment[]) {
    data.sort((a, b) => new Date(b.commentedDate).getTime() - new Date(a.commentedDate).getTime());
    this.comments = data;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
