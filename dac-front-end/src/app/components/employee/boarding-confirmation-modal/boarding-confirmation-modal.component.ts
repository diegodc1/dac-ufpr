import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-boarding-confirmation-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './boarding-confirmation-modal.component.html',
  styleUrls: ['./boarding-confirmation-modal.component.css']
})
export class BoardingConfirmationModalComponent {
  @Input() passageiro: any;
  @Output() confirmBoarding = new EventEmitter<void>();
  @Output() closeModal = new EventEmitter<void>();

  codigoReserva: string = '';
  reservaEncontrada = false;

  reserva = {
    codigo: 'ADC432',
    nome: 'Joao Silva',
    origem: 'Guarulhos - GRU',
    destino: 'Guarulhos - GRU'
  };

  buscarReserva() {
    this.reservaEncontrada = this.codigoReserva.trim().toUpperCase() === this.reserva.codigo;
  }

  confirmarEmbarque() {
    this.confirmBoarding.emit();
    this.closeModal.emit();
  }

  fecharModal() {
    this.closeModal.emit();
  }
}
