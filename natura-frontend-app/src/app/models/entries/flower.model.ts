import { Entry } from './entry.model';
import { User } from '../user.model';

export class Flower extends Entry {

    constructor(id: number = -1,
                name: string,
                createdBy: User) {
        super(id, name, createdBy);
    }
}