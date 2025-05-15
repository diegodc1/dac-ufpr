import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/auth'

  constructor(private http: HttpClient) {}

  // getUser(token: string) {
  //   const headers = new HttpHeaders().set('x-access-token', token);
  //   return this.http.get(`${this.apiUrl}/proximos`, { headers });
  // }

  getUser() {
    return this.http.get(`${this.apiUrl}/teste`);
  }
}
