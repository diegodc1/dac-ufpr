import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError, map } from 'rxjs';

// Definição da estrutura da reserva
interface Reserva {
  codigo: string;
  dataHora: string;
  aeroportoOrigem: string;
  aeroportoDestino: string;
  valorGasto: number;
  milhasGastadas: number;
  estado: 'CRIADA' | 'CHECK-IN' | 'CANCELADA';
}

interface Transacao {
  dataHora: string;
  valorEmReais: number;
  milhas: number;
  descricao: string;
  tipo: 'ENTRADA' | 'SAÍDA';
}
export interface BackendAirport {
  codigo: string;
  nome: string;
  cidade: string;
  uf: string;
}
export interface BackendFlightDetails {
  codigo: string;
  data: string;
  valor_passagem: number;
  quantidade_poltronas_total: number;
  quantidade_poltronas_ocupadas: number;
  estado: string;
  aeroporto_origem: BackendAirport;
  aeroporto_destino: BackendAirport;
}

export interface BackendReservationStatus {
  codigoEstado: number;
  descricaoEstado: string;
}
export interface BackendReservationWithFlight {
  codigo: string;
  codigoCliente: string;
  valor: number;
  milhasUtilizadas: number;
  quantidadePoltronas: number;
  codigoVoo: string;
  data: string;
  estado: BackendReservationStatus;
  voo: BackendFlightDetails;
}

export interface ReservaDTO {
  codigo_cliente: number;
  valor: number;
  milhas_utilizadas: number;
  quantidade_poltronas: number;
  codigo_voo: string | number;
}

@Injectable({
  providedIn: 'root',
})
export class ReservaService {


  private API_GATEWAY_URL = 'http://localhost:3000';
  private API_URL_RESERVAS = `${this.API_GATEWAY_URL}/reservas`;
  private API_URL_VOO = `${this.API_GATEWAY_URL}/voos`;
private API_URL_CLIENTES = `${this.API_GATEWAY_URL}/clientes`;


  private saldoMilhas: number = 10000;  // Saldo inicial de milhas
  private reservas: Reserva[] = [];     // Lista de reservas
  private transacoes: Transacao[] = []; // Lista de transações de milhas

  constructor(private http: HttpClient) {}

  private getAuthHeadersReq(): HttpHeaders | null {
    const authToken = localStorage.getItem('token');
    if (!authToken) {
      console.error('ClienteService: Token de autenticação não encontrado.');
      return null;
    }

    return new HttpHeaders({
      'Content-Type': 'application/json',
      'x-access-token': authToken}
    );
  }

  // Retorna o saldo de milhas atual
  getSaldoMilhas(): number {
    return this.saldoMilhas;
  }

  // Retorna todas as reservas
  getReservas(): Reserva[] {
    return this.reservas;
  }

  // Retorna uma reserva pelo código
  getReservaByCodigo(codigo: string): Reserva | undefined {
    return this.reservas.find((reserva) => reserva.codigo === codigo);
  }

  // Cria uma nova reserva
  criarReserva(reserva: ReservaDTO): Observable<any> {
    const headers = this.getAuthHeadersReq();
    if (!headers) {
      return throwError(() => new Error('Token de autenticação não encontrado.'));
    }
    return this.http.post(this.API_URL_RESERVAS, reserva, { headers });
  }

  // Gera um código de reserva único
  private gerarCodigoReserva(): string {
    const letras = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const numeros = '0123456789';
    let codigo = '';
    for (let i = 0; i < 3; i++) {
      codigo += letras.charAt(Math.floor(Math.random() * letras.length));
    }
    for (let i = 0; i < 3; i++) {
      codigo += numeros.charAt(Math.floor(Math.random() * numeros.length));
    }
    return codigo;
  }

  // Atualiza o saldo de milhas (para compra, cancelamento, etc.)
  private atualizarMilhas(milhas: number): void {
    this.saldoMilhas += milhas;
    // Registra a transação de milhas
    const transacao: Transacao = {
      dataHora: new Date().toLocaleString(),
      valorEmReais: 0, // O valor em reais é 0, pois estamos manipulando apenas as milhas
      milhas: milhas,
      descricao: milhas > 0 ? 'ENTRADA DE MILHAS' : 'SAÍDA DE MILHAS',
      tipo: milhas > 0 ? 'ENTRADA' : 'SAÍDA',
    };
    this.transacoes.push(transacao);
  }

  // Compra de milhas
  comprarMilhas(valorEmReais: number): void {
    if (valorEmReais <= 0) {
      console.error('O valor para a compra de milhas deve ser maior que zero.');
      return; // Evita compras inválidas
    }
    const milhasCompradas = Math.floor(valorEmReais / 5); // 1 milha para cada R$5
    this.atualizarMilhas(milhasCompradas);

    const transacao: Transacao = {
      dataHora: new Date().toLocaleString(),
      valorEmReais,
      milhas: milhasCompradas,
      descricao: 'COMPRA DE MILHAS',
      tipo: 'ENTRADA',
    };

    this.transacoes.push(transacao);
  }

  // Cancelar uma reserva
  cancelarReserva(reserva: Reserva): void {
    if (reserva.estado === 'CRIADA' || reserva.estado === 'CHECK-IN') {
      reserva.estado = 'CANCELADA';
      this.atualizarMilhas(reserva.milhasGastadas); // Retorna as milhas ao saldo
      const transacao: Transacao = {
        dataHora: new Date().toLocaleString(),
        valorEmReais: 0,
        milhas: reserva.milhasGastadas,
        descricao: `CANCELAMENTO DE RESERVA - ${reserva.aeroportoOrigem} -> ${reserva.aeroportoDestino}`,
        tipo: 'ENTRADA',
      };
      this.transacoes.push(transacao);
    } else {
      console.log('Reserva não pode ser cancelada, pois o estado não é válido.');
      // Adicione lógica para tratar reservas já canceladas ou concluídas
    }
  }

  // Ver o extrato de transações (compra de milhas, cancelamento, etc.)
  getExtratoTransacoes(): Transacao[] {
    return this.transacoes;
  }

    private getAuthHeaders(): HttpHeaders | null {
        const authToken = localStorage.getItem('token');
        if (!authToken) {
          console.error('ClienteService: Token de autenticação não encontrado.');
          return null;
        }

        return new HttpHeaders({
          'Content-Type': 'application/json',
          'x-access-token': authToken}
        );
   }
     private handleError(error: any): Observable<never> {
        console.error('Um erro ocorreu no ClienteService:', error);
        let errorMessage = 'Ocorreu um erro desconhecido.';
        if (error.error instanceof ErrorEvent) {
           errorMessage = `Erro: ${error.error.message}`;
        } else {
           errorMessage = `Código do erro: ${error.status}\nMensagem: ${error.message || error.error?.mensagem || error.error?.message}`;
        }
        return throwError(() => new Error(errorMessage));
     }

  getReservationsFromBackend(codigoCliente: string): Observable<BackendReservationWithFlight[]> {
    const headers = this.getAuthHeaders();

    if (!headers) {
      return throwError(() => new Error('Não foi possível obter cabeçalhos de autenticação.'));
    }
    const url = `${this.API_URL_CLIENTES}/${codigoCliente}/reservas`;
    console.log(`ReservaService (Backend): Buscando reservas do cliente ${codigoCliente} na URL: ${url}`);

    return this.http.get<BackendReservationWithFlight[]>(url, { headers, observe: 'response' }).pipe(
      map(response => {
        if (response.status === 204 || response.body === null || response.body === undefined) {
          console.warn('ReservaService: Resposta 204 No Content ou corpo vazio recebido. Retornando array vazio.');
          return [];
        }
        return Array.isArray(response.body) ? response.body : [];
      }),
      catchError(this.handleError)
    );
  }


   updateReservationStatusOnBackend(codigoReserva: string, novoEstado: string): Observable<any> {
    const headers = this.getAuthHeaders();
    if (!headers) {
      return throwError(() => new Error('Não foi possível obter cabeçalhos de autenticação.'));
    }
    const url = `${this.API_URL_RESERVAS}/${codigoReserva}/estado`;
    const body = { estado: novoEstado };
    console.log(`ReservaService (Backend): Atualizando estado da reserva ${codigoReserva} para ${novoEstado}`);
    return this.http.patch<any>(url, body, { headers, observe: 'response' }).pipe(
      map(response => {

        if (response.status === 204 || response.body === null || response.body === undefined) {
          console.warn('ReservaService: Resposta PATCH 204 No Content ou corpo vazio. Retornando objeto vazio.');
          return {};
        }
        return response.body;
      }),
      catchError(this.handleError)
    );
  }
}
