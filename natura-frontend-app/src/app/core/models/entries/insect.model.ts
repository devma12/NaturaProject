import { Entry } from './entry.model';
import { User } from '../user.model';

export class Insect extends Entry {

    constructor(name: string,
                createdBy: User = null,
                id: number = -1) {
        super(name, createdBy, id);
    }
}