import { Component, OnInit } from '@angular/core';
import { BoardingConfirmationModalComponent } from "../boarding-confirmation-modal/boarding-confirmation-modal.component";
import { DatePipe, NgForOf, NgIf } from "@angular/common";
import { HeaderEmployeeComponent } from "../header-employee/header-employee.component";
import { FormsModule } from "@angular/forms";
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { NgForm } from '@angular/forms';
import { FuncionarioService } from '../../../services/funcionario/funcionario.service';
import { Funcionario } from '../../../models/funcionario/funcionario';
import { NovoFuncionario } from '../../../models/novo-funcionario/novo-funcionario';
import { FuncCriado } from '../../../models/func-criado/func-criado';
import { RetornoFunc } from '../../../models/retorno-func/retorno-func.model';
import { FuncAlterado } from '../../../models/func-alterado/func-alterado';


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
export class ListEmployeesComponent implements OnInit {


  novoFuncionario!: NovoFuncionario;
  funcCriado!: FuncCriado;
  retornoFunc!: RetornoFunc;
  funcionarios: Funcionario[] = []
  showModal = false;
  showEditModal = false;
  showConfirmModal: boolean = false;
  selectedEmployee!: FuncAlterado;
  selectedEmployeeToRemove: any = null;
  phoneMask: string = '(00) 00000-0000';
  senhaMask: string = '0000';
  newEmployee: Employee = {
    id: 0,
    nome: '',
    cpf: '',
    email: '',
    telefone: ''
  };


  constructor(
    private servicoFuncionario: FuncionarioService
  ) { }

  ngOnInit(): void {
    this.listarFuncionarios();
    this.novoFuncionario = new NovoFuncionario();
  }

  editEmployee(funcionario: Funcionario) {
    this.selectedEmployee = FuncAlterado.fromFuncionario(funcionario);
    this.showEditModal = true;
  }

  updateEmployee(form: NgForm) {
    if (form.invalid) return;

    this.servicoFuncionario.alteraFuncionario(this.selectedEmployee).subscribe({
      next: (funcAtualizado) => {
        this.retornoFunc = funcAtualizado;
        console.log("Func atualizado: " + JSON.stringify(this.retornoFunc));
        this.closeEditModal();

      },
      error: (err) => {
        console.error("Erro ao atualizar funcionário: ", err);
      },
      complete: () => { this.listarFuncionarios(); },
    });
  }

  confirmRemove(funcionario: Funcionario) {
    this.selectedEmployeeToRemove = funcionario;
    this.showConfirmModal = true;
  }

  removeEmployeeConfirmed(): void {
    if (!this.selectedEmployeeToRemove) return;

    this.servicoFuncionario.remover(this.selectedEmployeeToRemove.codigo).subscribe({
      next: (funcRemovido) => {
        this.retornoFunc = funcRemovido;
        console.log("Veio do back: " + JSON.stringify(this.retornoFunc));
        this.selectedEmployeeToRemove = null;
        this.showConfirmModal = false;
      },
      complete: () => { this.listarFuncionarios(); }
    });
  }

  listarFuncionarios(): Funcionario[] {
    this.servicoFuncionario.listarTodos().subscribe({
      next: (listaFuncionarios: Funcionario[]) => {
        if (listaFuncionarios == null) {
          this.funcionarios = [];
        }
        else {
          this.funcionarios = listaFuncionarios;
        }
      }
    });
    return this.funcionarios;
  }

  addEmployee(form: NgForm): void {
    if (form.invalid) {
      console.log("erro")
      return;
    }

    this.servicoFuncionario.novoFuncionario(this.novoFuncionario).subscribe({
      next: (novofunc) => {
        this.retornoFunc = novofunc;
        console.log("Resposta do servidor: ", novofunc);
        console.log("Veio do back: " + JSON.stringify(this.retornoFunc));

        // Limpar o formulário e fechar o modal só após a resposta
        this.novoFuncionario.cpf = null;
        this.novoFuncionario.email = null;
        this.novoFuncionario.nome = null;
        this.novoFuncionario.telefone = null;
        this.closeModal();

        // Atualizar a lista de funcionários
        this.listarFuncionarios();
      },
      error: (err) => {
        console.error("Erro ao criar funcionário: ", err);
      },
      complete: () => { this.listarFuncionarios(); },
    });

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
