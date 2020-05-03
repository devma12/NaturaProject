import { Image } from '../image.model';
import { User } from '../user.model';
import { ValidableItem } from '../validable-item.model';

export abstract class Entry extends ValidableItem {

    public id: number;
    public name: string;
    public createdBy: User;
    public image: Image;
    public description: string;
    public location: string;

    constructor( name: string,
                createdBy: User = null,
                id: number = -1) {
        super();
        this.id = id;
        this.name = name;
        if (createdBy)
            this.createdBy = createdBy;

        this.description = '';
        this.location = '';
    }
}