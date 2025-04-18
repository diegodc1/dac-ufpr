import { Component, OnInit } from '@angular/core';
import { ReservaService } from '../../services/reserva.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { RouterLink } from '@angular/router';
import { MakeReservationComponent } from '../make-reservation/make-reservation.component';
import { format, addMinutes } from 'date-fns';
import { ptBR } from 'date-fns/locale';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HeaderComponent, MakeReservationComponent, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  saldoMilhas: number = 1000;
  reservas: any[] = [];
  reservasFiltradas: any[] = [];
  mostrarAcoes: boolean = true;

  constructor(private reservaService: ReservaService) {}

  ngOnInit() {
    this.carregarReservas();
  }

  carregarReservas() {
    this.reservas = [
      {
        codigo: 'ABC123',
        dataHora: '2025-04-10T10:00:00',
        aeroportoOrigem: 'Aeroporto de São Paulo (GRU)',
        aeroportoDestino: 'Aeroporto do Rio de Janeiro (GIG)',
        estado: 'CRIADA',
      },
      {
        codigo: 'DEF456',
        dataHora: '2025-04-12T15:30:00',
        aeroportoOrigem: 'Aeroporto de Brasília (BSB)',
        aeroportoDestino: 'Aeroporto de Recife (REC)',
        estado: 'CHECK-IN',
      },
      {
        codigo: 'GHI789',
        dataHora: '2025-04-15T08:00:00',
        aeroportoOrigem: 'Aeroporto de Salvador (SSA)',
        aeroportoDestino: 'Aeroporto de Fortaleza (FOR)',
        estado: 'CANCELADA',
      },
      {
        codigo: 'JKL012',
        dataHora: '2025-04-20T18:00:00',
        aeroportoOrigem: 'Aeroporto de Porto Alegre (POA)',
        aeroportoDestino: 'Aeroporto de Curitiba (CWB)',
        estado: 'CRIADA',
      },
    ];

    this.reservasFiltradas = this.reservas;
  }

  formatarData(data: string, formato: string): string {
    const dataObj = new Date(data);
    return format(dataObj, formato);
  }

  calcularHoraChegada(dataHora: string, horas: number, minutos: number): string {
    const dataPartida = new Date(dataHora);
    const chegada = addMinutes(dataPartida, horas * 60 + minutos);
    return format(chegada, 'HH:mm');
  }

  cancelarReserva(reserva: any) {
    if (reserva.estado === 'CRIADA') {
      reserva.estado = 'CANCELADA';
      this.reservaService.cancelarReserva(reserva);  
      this.carregarReservas();
    }
  }

  getStatusTexto(estado: string): string {
    switch (estado) {
      case 'CRIADA': return 'RESERVADO';
      case 'CHECK-IN': return 'REALIZADO';
      case 'CANCELADA': return 'CANCELADO';
      default: return estado;
    }
  }
}
