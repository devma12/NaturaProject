import { SpeciesType } from './type.enum';
import { ValidableItem } from './validable-item.model';
import { Phenology } from './phenology.model';
import { Criteria } from './criteria.model';

export class Species extends ValidableItem {

    public id: number;
    public commonName: string;
    public scientificName: string;
    public type: SpeciesType;
    public order: string;
    public family: string;
    public phenologies: Phenology[];
    public habitatType: string;
    public criteria: Criteria[];

    constructor(commonName: string,
                scientificName: string,
                type: SpeciesType) {
        super();
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.type = type;
        this.phenologies = [];
        this.criteria = [];
    }
}