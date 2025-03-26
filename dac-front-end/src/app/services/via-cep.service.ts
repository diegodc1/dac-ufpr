import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ViaCepResponse } from '../interfaces/via-cep-response.interface';


@Injectable({
  providedIn: 'root'
})
export class ViaCepService {

  private baseUrl = 'https://viacep.com.br/ws/';

  constructor(private http: HttpClient) {}

  getAddressByCep(cep: string): Observable<ViaCepResponse> {
    return this.http.get<ViaCepResponse>(`${this.baseUrl}${cep}/json/`);
  }
}
