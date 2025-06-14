export class NovoFuncionario {
    constructor(
        public cpf: string | null = null,
        public email: string | null = null,
        public nome: string | null = null,
        public telefone: string | null = null
    ) { }
}
