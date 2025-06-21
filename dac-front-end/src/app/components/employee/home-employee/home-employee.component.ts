import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderEmployeeComponent } from "../header-employee/header-employee.component";
import { CommonModule, DatePipe, NgForOf, NgIf } from "@angular/common";
import { BoardingConfirmationModalComponent } from "../boarding-confirmation-modal/boarding-confirmation-modal.component";
import { ca, th } from 'date-fns/locale';
import { CancelFlightModalComponent } from "../cancel-flight-modal/cancel-flight-modal.component";
import { FlightCompletionModalComponent } from "../flight-completion-modal/flight-completion-modal.component";
import { CreateFlightModalComponent } from "../create-flight-modal/create-flight-modal.component";
import { HttpClientModule } from '@angular/common/http';
import { FlightService } from '../../../services/flight.service';
import { Voo } from '../../../models/voo/voo.model';
import { FuncionarioService } from '../../../services/funcionario/funcionario.service';
import { catchError, Observable } from 'rxjs';
import Swal from "sweetalert2";



interface ModalData {
    codigo?: number;
  }

@Component({
  selector: 'app-home-employee',
  standalone: true,
  imports: [
    HeaderEmployeeComponent,
    NgForOf,
    NgIf,
    CommonModule,
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
   private baseUrl = "";
  http: any;

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
    }

  )


  }


  modals = {
    boarding: { isOpen: false, data: null as Voo | null },
    cancelFlight: { isOpen: false, data: null as Voo | null },
    flightCompletion: { isOpen: false, data: null as Voo | null },
    createFlight: { isOpen: false, data: null as any }
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


  handleCreateFlight(flightData: any) {
    console.log('Novo voo cadastrado:', flightData);
    Swal.fire({
      icon: 'success',
      title: 'Voo Cadastrado!',
      text: `O voo foi cadastrado com sucesso!`,
      confirmButtonColor: '#3085d6',
    });
    this.closeModal('createFlight');
    this.listarVoosProx48h();
  }

 handleCancelFlight() {
    const vooToCancel: Voo | null = this.modals.cancelFlight.data;
    if (vooToCancel && vooToCancel.codigo) {
      console.log('Solicitando cancelamento para o voo:', vooToCancel.codigo);
      this.servicoFuncionario.patchFlightState(vooToCancel.codigo, 'CANCELADO').subscribe({
        next: (updatedVoo: Voo) => {
          Swal.fire({
            icon: 'success',
            title: 'Voo Cancelado!',
            text: `Este Voo foi cancelado com sucesso!`,
            confirmButtonColor: '#3085d6',
          });
          const index = this.voos.findIndex(v => v.codigo === updatedVoo.codigo);
          if (index > -1) {
            this.voos[index].estado = updatedVoo.estado;
          }
          this.closeModal('cancelFlight');
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Erro!',
            text: `Ocorreu um erro ao cancelar este voo!`,
            confirmButtonColor: '#3085d6',
          });
          this.closeModal('cancelFlight');
        }
      });
    } else {
      console.warn('Nenhum voo selecionado para cancelar ou código ausente.');
      this.closeModal('cancelFlight');
    }
  }

  handleFlightCompletion() {
    const vooToComplete: Voo | null = this.modals.flightCompletion.data;
    if (vooToComplete && vooToComplete.codigo) {
      console.log('Solicitando realização do voo:', vooToComplete.codigo);
      this.servicoFuncionario.patchFlightState(vooToComplete.codigo, 'REALIZADO').subscribe({
        next: (updatedVoo: Voo) => {
          Swal.fire({
            icon: 'success',
            title: 'Voo Realizado!',
            text: `O Voo foi marcado como realizado com sucesso!`,
            confirmButtonColor: '#3085d6',
          });
          const index = this.voos.findIndex(v => v.codigo === updatedVoo.codigo);
          if (index > -1) {
            this.voos[index].estado = updatedVoo.estado;
          }
          this.closeModal('flightCompletion');
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'ERRO',
            text: `Erro ao marcar voo como realizado! ${err.message}`,
            confirmButtonColor: '#3085d6',
          });
          this.closeModal('flightCompletion');
        }
      });
    } else {
      console.warn('Nenhum voo selecionado para realizar ou código ausente.');
      this.closeModal('flightCompletion');
    }
  }
}


