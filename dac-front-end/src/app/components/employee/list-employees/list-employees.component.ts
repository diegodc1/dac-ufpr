import { Component } from '@angular/core';
import {BoardingConfirmationModalComponent} from "../boarding-confirmation-modal/boarding-confirmation-modal.component";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {HeaderEmployeeComponent} from "../header-employee/header-employee.component";
import {FormsModule} from "@angular/forms";
import { NgxMaskDirective,NgxMaskPipe, provideNgxMask  } from 'ngx-mask';
import { NgForm } from '@angular/forms';



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
    NgIf,
    FormsModule,
    NgxMaskDirective,
    NgxMaskPipe,
  ],
  templateUrl: './list-employees.component.html',
  styleUrl: './list-employees.component.css',
  providers: [provideNgxMask()],
})
export class ListEmployeesComponent {
  showModal = false;
  showEditModal = false;
  showConfirmModal: boolean = false;

  selectedEmployee: any = {};
  selectedEmployeeToRemove: any = null;

  phoneMask: string = '(00) 00000-0000';


  employees: Employee[] = [
    { id: 1, nome: 'João', cpf: '11111111111', email: 'joao@gmail.com', telefone: '41999999999'},
    { id: 2, nome: 'Maria', cpf: '22222222222', email: 'maria@gmail.com', telefone: '41999999999'},
    { id: 3, nome: 'Pedrinho', cpf: '33333333333', email: 'pedrinho@gmail.com', telefone: '41999999999' },
    { id: 4, nome: 'Joana', cpf: '44444444444U', email: 'joana@gmail.com', telefone: '41999999999' }
  ];

  newEmployee: Employee = {
    id: 0,
    nome: '',
    cpf: '',
    email: '',
    telefone: ''
  };

  addEmployee(form: NgForm): void {
    if (form.invalid) {
      console.log("erro")
      return;
    }

    this.employees.push({ ...this.newEmployee });
    this.newEmployee = { id: 0, nome: '', cpf: '', email: '', telefone: '' };
    this.closeModal();
  }

  updateEmployee(form: NgForm) {
    if (form.invalid) return;

    const index = this.employees.findIndex(e => e.id === this.selectedEmployee.id);
    if (index !== -1) {
      this.employees[index] = { ...this.selectedEmployee };
    }

    this.closeEditModal();
  }

  editEmployee(employee: any) {
    this.selectedEmployee = { ...employee };
    this.showEditModal = true;
  }



  confirmRemove(employee: any) {
    this.selectedEmployeeToRemove = employee;
    this.showConfirmModal = true;
  }

  removeEmployeeConfirmed() {
    const index = this.employees.indexOf(this.selectedEmployeeToRemove);
    if (index > -1) {
      this.employees.splice(index, 1);
    }
    this.selectedEmployeeToRemove = null;
    this.showConfirmModal = false;
  }

  cancelRemove() {
    this.selectedEmployeeToRemove = null;
    this.showConfirmModal = false;
  }



  openModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.resetForm();
  }

  closeEditModal() {
    this.showEditModal = false;
  }

  resetForm() {
    this.newEmployee = {
      id: 0,
      nome: '',
      cpf: '',
      email: '',
      telefone: ''
    };
  }

}
