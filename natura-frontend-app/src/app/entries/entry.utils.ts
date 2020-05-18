import { ActivatedRoute, UrlSegment } from '@angular/router';
import { SpeciesType } from '../models/type.enum';

export class EntryUtils {

    public static getEntryTypeFromRoute(route: ActivatedRoute): SpeciesType {
        let type: SpeciesType;
        const url: UrlSegment = route.snapshot.url[route.snapshot.url.length - 1];
        console.log(url.toString());
        if (url.toString() === 'flower') {
            type = SpeciesType.Flower;
        } else if (url.toString() === 'insect') {
            type = SpeciesType.Insect;
        } else {
            throw new Error('unknown type');
        }
        return type;
    }

}