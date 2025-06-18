import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { LoginService } from '../login/login.service';
import { Funcionario } from '../../models/funcionario/funcionario';
import { NovoFuncionario } from '../../models/novo-funcionario/novo-funcionario';
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

   private getAuthHeaders(useXAccessToken: boolean = false): HttpHeaders {
    const usuarioLogado = this.loginService.usuarioLogado;
    let token: string | null = null;

    if (usuarioLogado && usuarioLogado.access_token) {
      token = usuarioLogado.access_token;
    }

    if (!token) {
      console.error('ERRO: Token de autenticação não encontrado no LoginService ao tentar obter cabeçalhos!');
      // Em um cenário real, você pode querer redirecionar para a página de login aqui
      // this.router.navigate(['/login']); // Se você injetar o Router
      return new HttpHeaders({ 'Content-Type': 'application/json' });
    }

    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    if (useXAccessToken) {
      headers = headers.set('x-access-token', `${token}`);
    } else {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  listarVoosProx48h<T>(id: string): Observable<T> {
    // Usamos `getAuthHeaders(true)` para manter a compatibilidade com 'x-access-token' se necessário
    const headers = this.getAuthHeaders(true);
    const dataInicio = '2025-05-10'; // Lembre-se de que estas datas são fixas no seu código
    const dataFim = '2025-05-15';   // Você pode precisar torná-las dinâmicas
    return this.httpClient.get<T>(`${this.BASE_URL}/voos?data=${dataInicio}&data-fim=${dataFim}`, { headers });
  }

  patchFlightState(codigoVoo: number, estado: 'CANCELADO' | 'REALIZADO'): Observable<Voo> {
    const payload = { estado: estado };
    // Usamos `getAuthHeaders()` sem `true` para usar o 'Authorization: Bearer' (recomendado)
    const headers = this.getAuthHeaders();
    return this.httpClient.patch<Voo>(`${this.BASE_URL}/voos/${codigoVoo}/estado`, payload, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  listarTodos(): Observable<Funcionario[]> {
    const headers = this.getAuthHeaders(true); // Mantém 'x-access-token'
    return this.httpClient.get<Funcionario[]>(this.BASE_URL + '/funcionarios', { headers });
  }

  novoFuncionario(novo: NovoFuncionario): Observable<any> {
    console.log('chegou no servico: ' + novo);
    const headers = this.getAuthHeaders(true); // Mantém 'x-access-token'
    return this.httpClient.post<NovoFuncionario>(this.BASE_URL + '/funcionarios', JSON.stringify(novo), { headers });
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
