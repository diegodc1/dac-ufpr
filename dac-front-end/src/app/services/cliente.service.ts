import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private API_GATEWAY_URL = 'http://localhost:3000';

  constructor(private http: HttpClient) { }

  getSaldoMilhas(): Observable<number> {
    return this.http.get<number>(`${this.API_GATEWAY_URL}/clientes/saldo-milhas`);
  }

  listarReservas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_GATEWAY_URL}/reservas`);
  }

  listarVoos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_GATEWAY_URL}/voos`);
  }

  cancelarReserva(codigoReserva: string): Observable<any> {
    return this.http.delete(`${this.API_GATEWAY_URL}/reservas/${codigoReserva}`);
  }

  // R03 - Tela Inicial do Cliente
  getTelaInicialCliente(clienteId: string): Observable<any> {
    return this.http.get<any>(`${this.API_GATEWAY_URL}/clientes/home?clienteId=${clienteId}`);
  }

  // R04 - Tela de Detalhes da Reserva
  getDetalhesReserva(codigoReserva: string): Observable<any> {
    return this.http.get<any>(`${this.API_GATEWAY_URL}/reservas/${codigoReserva}`);
}

 // R05 - comprar milhas
comprarMilhas(clienteId: string, valorEmReais: number): Observable<any> {
  return this.http.post<any>(`${this.API_GATEWAY_URL}/clientes/comprar-milhas`, {
      clienteId,
      valorEmReais,
  });
}
}
