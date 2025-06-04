import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginData } from '../../models/login/login-data.model';
import { Usuario } from '../../models/usuario/usuario.model';

const DAC_LOGIN_KEY: string = "usuarioLogado";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  BASE_URL = "http://localhost:3000";

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private httpClient: HttpClient) { }

  public get usuarioLogado(): Usuario | null {
    let usuario = localStorage[DAC_LOGIN_KEY];
    return (usuario ? JSON.parse(localStorage[DAC_LOGIN_KEY]) : null);
  }

  public set usuarioLogado(usuario: Usuario) {
    localStorage[DAC_LOGIN_KEY] = JSON.stringify(usuario);
  }

  login(login: LoginData): Observable<Usuario> {

    let usuarioLogado = this.httpClient.post<Usuario>(this.BASE_URL + '/login', JSON.stringify(login), this.httpOptions);
    return usuarioLogado;
  }

}
