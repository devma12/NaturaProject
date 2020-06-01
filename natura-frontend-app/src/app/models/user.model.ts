export class User {

    public id: number;
    public email: string;
    public username: string;
    public token: string;
    public flowerValidator: boolean;
    public insectValidator: boolean;

    constructor(id: number = -1,
                email: string,
                username: string,
                flowerValidator: boolean = false,
                insectValidator: boolean = false) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.flowerValidator = flowerValidator;
        this.insectValidator = insectValidator;
    }
}