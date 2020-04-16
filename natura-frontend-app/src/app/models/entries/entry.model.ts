import { User } from '../user.model';
import { ValidableItem } from '../validable-item.model';

export abstract class Entry extends ValidableItem {

    public id: number;
    public name: string;
    public createdBy: User;

    constructor(id: number = -1,
                name: string,
                createdBy: User) {
        super();
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
    }
}