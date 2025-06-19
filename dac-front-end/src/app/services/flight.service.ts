import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlightService {
 private API_URL_BASE = 'http://localhost:3000';
 private API_URL_VOOS = `${this.API_URL_BASE}/voos`;
 private API_URL_AEROPORTOS = `${this.API_URL_BASE}/aeroportos`; 

constructor(private http : HttpClient) { }


createFlight(flightData : any, authToken: string): Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'x-access-token': authToken 
    });
    return this.http.post<any>(this.API_URL_VOOS, flightData, { headers });
  }


  getAeroportos(authToken: string): Observable<any[]> { 
   const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'x-access-token': authToken 
  });
   return this.http.get<any[]>(this.API_URL_AEROPORTOS, {headers}).pipe(
    catchError(this.handleError) 
);
 }

 searchFlights(origem: string, destino: string, dataAtual: string, authToken: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'x-access-token': authToken 
    });

     let url = `${this.API_URL_VOOS}`;

    // if (origem) {
    //   url += `&origem=${origem}`;
    // }
    // if (destino) {
    //   url += `&destino=${destino}`;
    // }

    return this.http.get<any>(url, { headers: headers }).pipe(
      catchError(this.handleError)
    );
  }


 private handleError(error: any): Observable<never> {
  console.error('Um erro ocorreu no FlightService:', error);
  let errorMessage = 'Ocorreu um erro desconhecido.';
   if (error.error instanceof ErrorEvent) {
    errorMessage = `Erro: ${error.error.message}`;
   } else {
   errorMessage = `Código do erro: ${error.status}\nMensagem: ${error.message || error.error?.mensagem || error.error?.message}`;
   }
 return throwError(() => new Error(errorMessage));
 }
 
}
