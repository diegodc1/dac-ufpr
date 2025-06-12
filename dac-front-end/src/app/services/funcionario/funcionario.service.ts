import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { LoginService } from '../login/login.service';
import { Voo } from '../../models/voo/voo.model';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  http: any;

  constructor(
    private httpClient: HttpClient,
    private loginService: LoginService
  ) { }

  BASE_URL = "http://localhost:3000";

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  setarToken() {
    let token = this.loginService.usuarioLogado?.access_token;
    this.httpOptions.headers = this.httpOptions.headers.set('x-access-token', `${token}`);
  }

  listarVoosProx48h<T>(id: string): Observable<T> {
    this.setarToken();
    const dataInicio = '2025-05-10';
    const dataFim = '2025-05-15';
    return this.httpClient.get<T>(`${this.BASE_URL}/voos?data=${dataInicio}&data-fim=${dataFim}`, this.httpOptions);
  }
  patchFlightState(codigoVoo: number, estado: 'CANCELADO' | 'REALIZADO'): Observable<Voo> {
    const payload = { estado: estado };
    const headers = this.getHeaders(); // Obtém o token

    return this.httpClient.patch<Voo>(`${this.BASE_URL}/voos/${codigoVoo}/estado`, payload, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('funcionarioToken');
    if (!token) {
      console.error('Token de autenticação não encontrado!');
      return new HttpHeaders({ 'Content-Type': 'application/json' });
    }
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  private handleError(error: any): Observable<never> {
    console.error('Um erro ocorreu:', error);
    let errorMessage = 'Ocorreu um erro desconhecido.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      errorMessage = `Código do erro: ${error.status}\nMensagem: ${error.message || error.error.mensagem || error.error.message}`;
      if (error.status === 401 || error.status === 403) {
        errorMessage = 'Não autorizado ou acesso negado. Verifique suas permissões.';
      } else if (error.status === 404) {
        errorMessage = 'Recurso não encontrado.';
      }
    }
    return throwError(() => new Error(errorMessage));
  }
}
