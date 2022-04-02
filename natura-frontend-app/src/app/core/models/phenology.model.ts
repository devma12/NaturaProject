import { Month } from './month.enum';

export class Phenology {

    public id: number;
    public start: Month;
    public end: Month;

    constructor(start: Month,
                end: Month) {
        this.start = start;
        this.end = end;
    }
}

