export class Criteria {

    public id: number;
    public name: string;
    public value: string;

    constructor(id: number = -1,
                name: string,
                value: string) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}