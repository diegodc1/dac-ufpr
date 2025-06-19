import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {HeaderComponent} from "../header/header.component";
import { FlightService } from '../../services/flight.service';
import { th } from 'date-fns/locale';
import { ClienteService } from '../../services/cliente.service';

interface Voo {
  codigo: number;
  data: string; 
  valor_passagem: number;
  quantidade_poltronas_total: number;
  quantidade_poltronas_ocupadas: number;
  estado: string;
  aeroporto_origem: {
    codigo: string;
    nome: string;
    cidade: string;
    uf: string;
  };
  aeroporto_destino: {
    codigo: string;
    nome: string;
    cidade: string;
    uf: string;
  };
}
@Component({
  selector: 'app-make-reservation',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './make-reservation.component.html',
  styleUrls: ['./make-reservation.component.css']
})
export class MakeReservationComponent {
  origemAeroportoCodigo: string = ''; 
  destinoAeroportoCodigo: string = ''; 
  aeroportos: any[] = []; 

  filteredFlights: Voo[] = [];

  constructor(private router: Router, private flightService: FlightService) {}

  // voos: Voo[] = [
  //   { id: 1, origem: 'São Paulo', destino: 'Nova York', dataHora: '29 Jan 2026, 14:50', preco: 3500 },
  //   { id: 2, origem: 'Rio de Janeiro', destino: 'Paris', dataHora: '29 Jan 2026, 15:00', preco: 4000 },
  //   { id: 3, origem: 'Mombasa', destino: 'Nairobi', dataHora: '29 Jan 2026, 14:30', preco: 12000 }
  // ];


  ngOnInit(): void{
    this.loadAeroportos();
    this.buscarVoos();
  }

private getAuthToken(): string | null {
    const usuarioString = localStorage.getItem('usuarioLogado');
    let authToken: string | null = null;

    if (usuarioString) {
      try {
        const usuario = JSON.parse(usuarioString);
        authToken = usuario.access_token;
      } catch (e) {
        console.error('Erro ao fazer parse do usuário do localStorage:', e);
      }
    }
    return authToken;
  }


  loadAeroportos(): void {
    const authToken = this.getAuthToken();
    if (authToken) {
      this.flightService.getAeroportos(authToken).subscribe({
        next: (data: any[]) => {
          this.aeroportos = data;
          console.log('Aeroportos carregados para busca de voos:', this.aeroportos);
        },
        error: (error: any) => {
          console.error('Erro ao carregar aeroportos para busca de voos:', error);
        }
      });
    } else {
      console.error('Token de autenticação não encontrado para carregar aeroportos na busca de voos.');
    }
  }

  buscarVoos() {
    const authToken = this.getAuthToken();
    if (!authToken) {
      console.error('Token de autenticação não encontrado para buscar voos.');
      this.filteredFlights = [];
      return;
    }

    const now = new Date();
    const formattedDate = now.toISOString().substring(0, now.toISOString().length - 5) + '+00:00';
    console.log('Data formatada para backend (buscarVoos):', formattedDate);

    this.flightService.searchFlights(this.origemAeroportoCodigo, this.destinoAeroportoCodigo, formattedDate, authToken).subscribe({
      next: (response: any) => {
        if (response && response.voos) {
          this.filteredFlights = response.voos.filter((voo: Voo) => voo.estado === 'CONFIRMADO');
          console.log('Voos CONFIRMADOS buscados:', this.filteredFlights);
        } else {
          this.filteredFlights = [];
          console.warn('Resposta da busca de voos não contém a propriedade "voos" ou está vazia.', response);
        }
      },
      error: (error: any) => {
        console.error('Erro ao buscar voos:', error);
        this.filteredFlights = [];
      }
    });
  }

  selecionarVoo(voo: Voo) {
    console.log(`Voo selecionado para compra: Código ${voo.codigo}`);
    this.router.navigate(['/confirm-reservation', voo.codigo]);
  }
}
