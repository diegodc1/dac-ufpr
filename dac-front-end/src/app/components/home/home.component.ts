import { Component, OnInit } from '@angular/core';
import { ReservaService } from '../../services/reserva.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { Router, RouterLink } from '@angular/router';
import { MakeReservationComponent } from '../make-reservation/make-reservation.component';
import { ModalCancelarReservaComponent } from '../modal-cancelar-reserva/modal-cancelar-reserva.component';
import { format, addMinutes } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    RouterLink,
    FormsModule,
    ModalCancelarReservaComponent,  // 👉 Importando o componente do modal
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  saldoMilhas: number = 1000;
  reservas: any[] = [];
  reservasFiltradas: any[] = [];
  mostrarAcoes: boolean = true;

  origem: string = '';
  destino: string = '';

  // 👉 Controle do modal
  mostrarModal: boolean = false;
  reservaSelecionada: any = null;

  constructor(
    private reservaService: ReservaService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarReservas();
  }

  carregarReservas() {
    this.reservas = [
      {
        codigo: 'ABC123',
        dataHora: '2025-04-10T10:00:00',
        aeroportoOrigem: ' São Paulo (GRU)',
        aeroportoDestino: ' Rio de Janeiro (GIG)',
        estado: 'RESERVADO',
        milhasGastadas:2000,
        valorGasto:180.00
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
        estado: 'CANCELADO',
      },
      {
        codigo: 'JKL012',
        dataHora: '2025-04-20T18:00:00',
        aeroportoOrigem: 'Porto Alegre (POA)',
        aeroportoDestino: 'Curitiba (CWB)',
        estado: 'RESERVADO',
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
    if (reserva.estado === 'RESERVADO') {
      this.reservaSelecionada = reserva;
      this.mostrarModal = true;
    }
  }

  confirmarCancelamento() {
    if (this.reservaSelecionada) {
      this.reservaSelecionada.estado = 'CANCELADO';
      this.reservaService.cancelarReserva(this.reservaSelecionada);
      this.mostrarModal = false;
  
      // Atualiza a reserva no array original
      const index = this.reservas.findIndex(r => r.codigo === this.reservaSelecionada.codigo);
      if (index !== -1) {
        this.reservas[index].estado = 'CANCELADO';
      }
    }
  }
  

  fecharModal() {
    this.mostrarModal = false;
  }

  getStatusTexto(estado: string): string {
    switch (estado) {
      case 'RESERVADO':
        return 'RESERVADO';
      case 'CHECK-IN':
        return 'REALIZADO';
      case 'CANCELADO':
        return 'CANCELADO';
      default:
        return estado;
    }
  }

  buscarVoos() {
    this.router.navigate(['/make-reservation'], {
      queryParams: {
        origem: this.origem,
        destino: this.destino,
      },
    });
  }
}
