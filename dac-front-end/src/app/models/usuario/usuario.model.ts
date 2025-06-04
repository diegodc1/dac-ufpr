export class Usuario {
    constructor(
        public userId?: string,
        public access_token?: string,
        public token_type?: string,
        public name?: string,
        public login?: string,
        public tipo?: string
    ) { }
}
