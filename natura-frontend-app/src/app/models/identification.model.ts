import { Entry } from './entries/entry.model';
import { Species } from './species.model';
import { User } from './user.model';

export class Identification {

    entry: Entry;
    species: Species;
    suggestedBy: User;
    suggestedDate: Date;
    validatedBy: User;
    validatedDate: Date;
    comments: Comment[];
    likes: User[];

    constructor(entry: Entry,
                species: Species,
                suggestedBy: User,
                suggestedDate: Date) {
        this.entry = entry;
        this.species = species;
        this.suggestedBy = suggestedBy;
        this.suggestedDate = suggestedDate;
        this.comments = [];
        this.likes = [];
    }
}