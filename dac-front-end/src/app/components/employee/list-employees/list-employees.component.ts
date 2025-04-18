import { Component } from '@angular/core';
import {BoardingConfirmationModalComponent} from "../boarding-confirmation-modal/boarding-confirmation-modal.component";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {HeaderEmployeeComponent} from "../header-employee/header-employee.component";


interface Employee {
  id: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string
}

@Component({
  selector: 'app-list-employees',
  standalone: true,
    imports: [
        BoardingConfirmationModalComponent,
        DatePipe,
        HeaderEmployeeComponent,
        NgForOf,
        NgIf
    ],
  templateUrl: './list-employees.component.html',
  styleUrl: './list-employees.component.css'
})
export class ListEmployeesComponent {

  employees: Employee[] = [
    { id: 1, nome: 'João', cpf: '111.111.111-11', email: 'joao@gmail.com', telefone: '41 99999-9999'},
    { id: 2, nome: 'Maria - GRU', cpf: '222.222.222-22', email: 'maria@gmail.com', telefone: '41 99999-9999'},
    { id: 3, nome: 'Pedrinho - GRU', cpf: '333.333.333-33', email: 'pedrinho@gmail.com', telefone: '41 99999-9999' },
    { id: 4, nome: 'Joana - GRU', cpf: '444.444.444-44U', email: 'joana@gmail.com', telefone: '41 99999-9999' }
  ];

}
