import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginService } from '../login/login.service';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {

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

}
