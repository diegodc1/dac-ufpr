import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReservaService, BackendReservationWithFlight } from '../../services/reserva.service';

@Component({
  selector: 'app-check-reservation',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './check-reservation.component.html',
  styleUrl: './check-reservation.component.css'
})
export class CheckReservationComponent {
  codigoReserva: string = '';
  reserva: BackendReservationWithFlight | null = null;
  erro: string | null = null;
  carregando: boolean = false;

  constructor(private reservaService: ReservaService) {}

  buscarReserva() {
    this.erro = null;
    this.carregando = true;
    this.reserva = null;
    this.reservaService.getReservaByCodigoBackend(this.codigoReserva).subscribe({
      next: (res) => {
        this.reserva = res;
        this.carregando = false;
      },
      error: (err) => {
        this.erro = err.message || 'Erro ao buscar reserva';
        this.carregando = false;
      }
    });
  }
}
