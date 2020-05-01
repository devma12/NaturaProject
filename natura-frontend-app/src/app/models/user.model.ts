export class User {

    public id: number;
    public email: string;
    public username: string;
    public token: string;

    constructor(id: number = -1,
                email: string,
                username: string) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}