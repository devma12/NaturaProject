import { SpeciesType } from './type.enum';
import { ValidableItem } from './validable-item.model';

export class Species extends ValidableItem {

    public id: number;
    public commonName: string;
    public scientificName: string;
    public type: SpeciesType;

    constructor(id: number = -1,
                commonName: string,
                scientificName: string,
                type: SpeciesType) {
        super();
        this.id = id;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.type = type;
    }
}