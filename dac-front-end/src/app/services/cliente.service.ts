import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private API_URL = 'http://localhost:8091/client';
  private API_GATEWAY_URL = 'http://localhost:3000';

  constructor(private http: HttpClient) { }

  cadastrarCliente(dados: any): Observable<any> {
    return this.http.post(`${this.API_GATEWAY_URL}/clientes`, dados);
  }

  getSaldoMilhas(): Observable<number> {
    return this.http.get<number>(`${this.API_URL}/saldo-milhas`);
  }

  getPerfilCliente(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/perfil`);
  }

  atualizarPerfil(perfil: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/perfil`, perfil);
  }

  comprarMilhas(quantidade: number, valorPago: number): Observable<any> {
    const authToken = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'x-access-token': authToken || ''
    });

    return this.http.post<any>(`${this.API_URL}/comprar-milhas`,
      { quantidade, valorPago },
      { headers }
    );
  }
}
