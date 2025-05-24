import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderEmployeeComponent } from "../header-employee/header-employee.component";
import { DatePipe, NgForOf, NgIf } from "@angular/common";
import { BoardingConfirmationModalComponent } from "../boarding-confirmation-modal/boarding-confirmation-modal.component";
import { ca, th } from 'date-fns/locale';
import { CancelFlightModalComponent } from "../cancel-flight-modal/cancel-flight-modal.component";
import { FlightCompletionModalComponent } from "../flight-completion-modal/flight-completion-modal.component";
import { CreateFlightModalComponent } from "../create-flight-modal/create-flight-modal.component";
import { HttpClientModule } from '@angular/common/http';
import { FlightService } from '../../../services/flight.service';
import { Voo } from '../../../models/voo/voo.model';
import { FuncionarioService } from '../../../services/funcionario/funcionario.service';


// interface Voo {
//   id: number;
//   origem: string;
//   destino: string;
//   dataHora: string;
//   estado: string
// }

@Component({
  selector: 'app-home-employee',
  standalone: true,
  imports: [
    HeaderEmployeeComponent,
    NgForOf,
    NgIf,
    DatePipe,
    HttpClientModule,
    BoardingConfirmationModalComponent,
    CancelFlightModalComponent,
    FlightCompletionModalComponent,
    CreateFlightModalComponent
  ],
  templateUrl: './home-employee.component.html',
  styleUrl: './home-employee.component.css'
})
export class HomeEmployeeComponent implements OnInit {

  idFuncionario!: string; // esta variável armazena o id que vem do mongo db no authentication service!
  voos: Voo[] = [];

  constructor(
    private route: ActivatedRoute,
    private servicoFuncionario: FuncionarioService
  ) { }

  ngOnInit(): void {
    this.idFuncionario = this.route.snapshot.params['id']; // pegamos o id que vem na rota
    this.listarVoosProx48h();
  }

  listarVoosProx48h(): void {
    this.servicoFuncionario.listarVoosProx48h<Voo[]>(this.idFuncionario).subscribe({
      next: (listaVoos: Voo[]) => {
        if (listaVoos == null) {
          this.voos = [];
        } else {
          this.voos = listaVoos;
        }
      },
      error: (err) => {
        console.error('Erro ao buscar voos', err);
        this.voos = [];
      }
    })
  }

  // voos: Voo[] = [
  //   { id: 1, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 14:50', estado: 'CONFIRMADO'},
  //   { id: 2, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 15:00', estado: 'CONFIRMADO'},
  //   { id: 3, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 14:30', estado: 'CONFIRMADO' },
  //   { id: 4, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 14:30', estado: 'CANCELADO' }
  // ];

  modals = {
    boarding: { isOpen: false, data: null },
    cancelFlight: { isOpen: false, data: null },
    flightCompletion: { isOpen: false, data: null },
    createFlight: { isOpen: false, data: null }
  };

  openModal(modalName: keyof typeof this.modals, data: any = null) {
    this.modals[modalName].isOpen = true;
    this.modals[modalName].data = data;
  }

  closeModal(modalName: keyof typeof this.modals) {
    this.modals[modalName].isOpen = false;
    this.modals[modalName].data = null;
  }

  handleConfirmBoarding() {
    console.log('Embarque confirmado para:', this.modals.boarding.data);
    this.closeModal('boarding');
  }

  handleCancelFlight() {
    console.log('Cancelando voo:', this.modals.cancelFlight.data);
    this.closeModal('cancelFlight');
  }

  handleFlightCompletion() {
    console.log('Voo marcado como realizado:', this.modals.flightCompletion.data);
    this.closeModal('flightCompletion');
  }

  handleCreateFlight(flightData: any) {
    console.log('Novo voo cadastrado:', flightData);
    this.closeModal('createFlight');
  }

}


