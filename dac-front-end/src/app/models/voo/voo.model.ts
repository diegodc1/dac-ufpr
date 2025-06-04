import { Aeroporto } from "../aeroporto/aeroporto.model";

export class Voo {

    constructor(
        public codigo?: number,
        public estado?: string,
        public data?: string,
        public aeroporto_destino?: Aeroporto,
        public quantidade_poltronas_ocupadas?: number,
        public quantidade_poltronas_total?: number,
        public aeroporto_origem?: Aeroporto,
        public valor_passagem?: number
    ) { }

}
