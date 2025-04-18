import { Component } from '@angular/core';
import {HeaderEmployeeComponent} from "../header-employee/header-employee.component";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import { BoardingConfirmationModalComponent } from "../boarding-confirmation-modal/boarding-confirmation-modal.component"; 


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
    BoardingConfirmationModalComponent
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

  isModalOpen = false; 
  passageiroSelecionado: any = null; 

  openModal(voo: Voo) {
    this.passageiroSelecionado = {
      codigo: voo.id,
      nome: 'Passageiro Exemplo', 
      origem: voo.origem,
      destino: voo.destino
    };
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.passageiroSelecionado = null;
  }

  handleConfirmBoarding() {
    console.log('Embarque confirmado para:', this.passageiroSelecionado);
    this.closeModal();
  }
  


}
