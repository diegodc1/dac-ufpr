import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../../services/flight.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { th } from 'date-fns/locale';

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
aeroportos: any[] = [];

  constructor(private flightService: FlightService) { }

  ngOnInit() : void{
    this.loadAeroportos();
  }

  loadAeroportos(): void {
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

    if (authToken) {
       this.flightService.getAeroportos(authToken).subscribe({ 
        next: (data: any[]) => {
          this.aeroportos = data;
        console.log('Aeroportos carregados no modal:', this.aeroportos);
        },
        error: (error: any) => {
        console.error('Erro ao carregar aeroportos no modal:', error);
        }
 });
    } else {
        console.error('Token de autenticação não encontrado para carregar aeroportos.');
    }
  }


  submitForm() {
   if (!this.flightData.dataHora) {
        alert('Por favor, preencha a Data e Hora do voo.');
        return;
    }

   const parsedDate = new Date(this.flightData.dataHora);
    if (isNaN(parsedDate.getTime())) {
        alert('Formato de Data e Hora inválido. Por favor, insira uma data e hora válidas.');
        return;
    }

    const isoString = parsedDate.toISOString();
    const formattedDateForBackend = isoString.substring(0, isoString.length - 5) + '+00:00';
    console.log('Data formatada para backend:', formattedDateForBackend); 

    const usuarioString = localStorage.getItem('usuarioLogado'); 
    let authToken: string | null = null;

    if (usuarioString) {
        try {
            const usuario = JSON.parse(usuarioString);
            authToken = usuario.access_token; 
        } catch (e) {
            console.error('Erro ao fazer parse do usuário do localStorage (submitForm):', e);
        }
    }

    if (!authToken) {
        console.error('Token de autenticação não encontrado para cadastrar voo. Certifique-se de estar logado.');
        alert('Sessão expirada ou não autenticada. Por favor, faça login novamente.');
        return; 
    }

    const vooDtoToBackend = {
        data: formattedDateForBackend, 
        codigo_aeroporto_origem: this.flightData.origem,
        codigo_aeroporto_destino: this.flightData.destino,
        valor_passagem: parseFloat(this.flightData.valor),
        quantidade_poltronas_total: parseInt(this.flightData.poltronas, 10),
        quantidade_poltronas_ocupadas: 0
    };
 console.log('Payload FINAL sendo enviado para o backend:', vooDtoToBackend);
    this.flightService.createFlight(vooDtoToBackend, authToken).subscribe({ 
      next: (response) => {
          console.log('Voo cadastrado com sucesso:', response);
          this.createFlight.emit(response);
          this.closeModal();
        },
        error: (error) => {
          console.error('Erro ao cadastrar voo:', error);
          alert(`Erro ao cadastrar voo: ${error.message || error.error?.message || JSON.stringify(error.error)}`);
        }
      });
  }

  closeModal() {
    this.closeModalEvent.emit();
  }
}
