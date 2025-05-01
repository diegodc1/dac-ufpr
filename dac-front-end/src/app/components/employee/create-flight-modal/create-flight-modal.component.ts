import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-flight-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-flight-modal.component.html',
  styleUrls: ['./create-flight-modal.component.css']
})
export class CreateFlightModalComponent {
  @Output() createFlight = new EventEmitter<any>();
  @Output() closeModalEvent = new EventEmitter<void>();

  flightData = {
    codigo: '',
    origem: '',
    destino: '',
    dataHora: '',
    valor: '',
    milhas: '',
    poltronas: ''
  };

  aeroportos = ['Guarulhos - GRU', 'Congonhas - CGH', 'Brasília - BSB', 'Galeão - GIG'];

  submitForm() {
    this.createFlight.emit(this.flightData);
  }

  closeModal() {
    this.closeModalEvent.emit();
  }
}
