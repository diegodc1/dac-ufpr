import { Component } from '@angular/core';
import {HeaderEmployeeComponent} from "../header-employee/header-employee.component";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import { BoardingConfirmationModalComponent } from "../boarding-confirmation-modal/boarding-confirmation-modal.component"; 
import { ca } from 'date-fns/locale';
import { CancelFlightModalComponent } from "../cancel-flight-modal/cancel-flight-modal.component";


interface Voo {
  id: number;
  origem: string;
  destino: string;
  dataHora: string;
  estado: string
}

@Component({
  selector: 'app-home-employee',
  standalone: true,
  imports: [
    HeaderEmployeeComponent,
    NgForOf,
    NgIf,
    DatePipe,
    BoardingConfirmationModalComponent,
    CancelFlightModalComponent
  ],
  templateUrl: './home-employee.component.html',
  styleUrl: './home-employee.component.css'
})
export class HomeEmployeeComponent {

  voos: Voo[] = [
    { id: 1, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 14:50', estado: 'CONFIRMADO'},
    { id: 2, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 15:00', estado: 'CONFIRMADO'},
    { id: 3, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 14:30', estado: 'CONFIRMADO' },
    { id: 4, origem: 'Guarulhos - GRU', destino: 'Guarulhos - GRU', dataHora: '29 Jan 2026, 14:30', estado: 'CANCELADO' }
  ];

  modals = {
    boarding: { isOpen: false, data: null },
    cancelFlight: { isOpen: false, data: null },
  };

  openModal(modalName: keyof typeof this.modals, data: any) {
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
  


}
