import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Reserva } from '../../models/reserva';
import { formatDate, CommonModule } from '@angular/common';
import { VerReservaComponent } from '../ver-reserva/ver-reserva.component';
import { DataFormatPipe } from '../../shared/pipes/data-format.pipe';
@Component({
  selector: 'app-modal-cancelar-reserva',
  standalone: true,
  templateUrl: './modal-cancelar-reserva.component.html',
  styleUrls: ['./modal-cancelar-reserva.component.scss'],
  imports: [CommonModule, DataFormatPipe],
})
export class ModalCancelarReservaComponent {
  @Input() reserva!: Reserva;
  @Input() mostrar: boolean = false;

  @Output() fechar = new EventEmitter<void>();
  @Output() confirmar = new EventEmitter<void>();

  onFechar(event?: MouseEvent): void {
    if (!event || (event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.fechar.emit();
    }
  }

  onConfirmar(): void {
    this.confirmar.emit();
  }

  getStatusTexto(estado?: string): string {
    switch (estado) {
      case 'RESERVADO':
        return 'RESERVADO';
      case 'CHECK-IN':
        return 'Check-in realizado';
      case 'CANCELADO':
        return 'Cancelado';
      default:
        return 'Desconhecido';
    }
  }

  

  calcularHoraChegada(data: string, horas: number, minutos: number): string {
    const dataPartida = new Date(data);
    dataPartida.setHours(dataPartida.getHours() + horas);
    dataPartida.setMinutes(dataPartida.getMinutes() + minutos);
    return formatDate(dataPartida, 'HH:mm', 'pt-BR');
  }
}
