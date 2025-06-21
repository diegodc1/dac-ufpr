import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private API_GATEWAY_URL = 'http://localhost:3000';

  constructor(private http: HttpClient) { }

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

  cadastrarCliente(cadastroClienteDTO: any): Observable<any> {
    return this.http.post<any>(`${this.API_GATEWAY_URL}/clientes`, cadastroClienteDTO);
  }

  getSaldoMilhas(codigoCliente: string): Observable<any> {
    const headers = this.getAuthHeaders()
    console.log(codigoCliente)
    if (!headers) return throwError(() => new Error('Autenticação necessária para buscar saldo.'));
    return this.http.get<any>(`${this.API_GATEWAY_URL}/clientes/saldo-milhas/${codigoCliente}`, { headers });
  }

  listarReservas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_GATEWAY_URL}/reservas`);
  }

  listarVoos(): Observable<any[]> {
      const headers = this.getAuthHeaders();
      if (!headers) return throwError(() => new Error('Autenticação necessária para Listar Voos.'));
          return this.http.get<any[]>(`${this.API_GATEWAY_URL}/voos`, { headers }).pipe(
          catchError(this.handleError)
      );
  }

  cancelarReserva(codigoReserva: string): Observable<any> {
    return this.http.delete(`${this.API_GATEWAY_URL}/reservas/${codigoReserva}`);
  }

  // R03 - Tela Inicial do Cliente
  getTelaInicialCliente(clienteId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    if (!headers) return throwError(() => new Error('Autenticação necessária para comprar milhas.'));

    return this.http.get<any>(`${this.API_GATEWAY_URL}/clientes/home/${clienteId}`, { headers });
  }

  // R04 - Tela de Detalhes da Reserva
  getDetalhesReserva(codigoReserva: string): Observable<any> {
    return this.http.get<any>(`${this.API_GATEWAY_URL}/reservas/${codigoReserva}`);
}

 // R05 - comprar milhas
  comprarMilhas(clienteId: string, quantidade: number, valorPago: number): Observable<any> {
    const headers = this.getAuthHeaders();
    if (!headers) return throwError(() => new Error('Autenticação necessária para comprar milhas.'));

    return this.http.put<any>(
      `${this.API_GATEWAY_URL}/clientes/${clienteId}/milhas`,
      { quantidade, valorPago},
      { headers }
    ).pipe(
      catchError(this.handleError)
    );
  }

  getExtratoMilhas(userEmail: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_GATEWAY_URL}/TransacaoMilhas/${userEmail}/extract`);
  }
}
