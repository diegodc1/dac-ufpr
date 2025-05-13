import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../../services/flight.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-create-flight-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
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
aeroportos: any;

  constructor(private flightService: FlightService) { }

  submitForm() {
    const formattedData = {
      ...this.flightData,
      data_hora: new Date(this.flightData.dataHora).toISOString()
    };

    const authToken = localStorage.getItem('authToken');

    if (authToken) {
      this.flightService.createFlight(formattedData, authToken).subscribe({
        next: (response) => {
          console.log('Voo cadastrado com sucesso:', response);
          this.createFlight.emit(response);
          this.closeModal();
        },
        error: (error) => {
          console.error('Erro ao cadastrar voo:', error);
        }
      });
    } else {
      console.error('Token de autenticação não encontrado.');
    }
  }

  closeModal() {
    this.closeModalEvent.emit();
  }
}
