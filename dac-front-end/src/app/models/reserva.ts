export interface Reserva {
    codigo: string;
    estado: 'RESERVADO' | 'CHECK-IN' | 'CANCELADO';
    aeroportoOrigem: string;
    aeroportoDestino: string;

    data: string;
    duracaoHoras: number;
    duracaoMinutos: number;

  valor: number;
  milhas_utilizadas: number;

    nomePassageiro?: string;
    assento?: string;
    emailUsuario?: string;

    dataCriacao?: string;
    ultimaAtualizacao?: string;
 }
