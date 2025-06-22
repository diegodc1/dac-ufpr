import { Funcionario } from "../funcionario/funcionario";

export class FuncAlterado {
    constructor(
        public codigo: number | null,
        public cpf: string | null,
        public email: string | null,
        public nome: string | null,
        public telefone: string | null,
        public senha: string | null
    ) { }

    static fromFuncionario(funcionario: Funcionario): FuncAlterado {
        return new FuncAlterado(
            funcionario.codigo,
            funcionario.cpf,
            funcionario.email,
            funcionario.nome,
            funcionario.telefone,
            null // senha inicializada como null
        );
    }
}
