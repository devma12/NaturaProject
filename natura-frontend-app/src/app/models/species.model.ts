import { ValidableItem } from './validable-item.model';

export class Species extends ValidableItem {

    public id: number;
    public commonName: string;
    public scientificName: string;

    constructor(id: number = -1,
                commonName: string,
                scientificName: string) {
        super();
        this.id = id;
        this.commonName = commonName;
        this.scientificName = scientificName;
    }
}