export abstract class ValidableItem {

    public isValidated: boolean;

    constructor(isValidated: boolean = false) {
        this.isValidated = isValidated;
    }
}