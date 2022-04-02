export abstract class ValidableItem {

    public validated: boolean;

    constructor(validated: boolean = false) {
        this.validated = validated;
    }
}