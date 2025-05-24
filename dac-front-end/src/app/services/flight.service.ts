import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlightService {

  private API_URL = 'http://localhost:3000/voos';
  constructor(private http : HttpClient) { }
  createFlight(flightData : any, authToken: string): Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'x-access-token': authToken 
    });
    return this.http.post<any>(this.API_URL, flightData, { headers });
  }
}
