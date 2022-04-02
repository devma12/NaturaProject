import { User } from './user.model';

export class Comment {

    text: string;
    commentedBy: User;
    commentedDate: Date;

    constructor(text: string,
                user: User,
                date: Date) {
        this.text = text;
        this.commentedBy = user;
        this.commentedDate = date;
    }
}