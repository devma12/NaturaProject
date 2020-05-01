import { ActivatedRoute, UrlSegment } from '@angular/router';
import { SpeciesType } from '../models/type.enum';
import { Entry } from '../models/entries/entry.model';
import { Image } from '../models/image.model';

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

    public static getEntryPictureBase64Data(entry: Entry): any {
        if (entry) {
            // get image
            console.log(entry.name);
            let image: Image = entry.image;
            if (image) {
                const base64Data = image.data;
                const type: string = image.type;
                return `data:${type};base64,${base64Data}`;
            }
        }
        return {};
    }

}