import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {CurrencyPipe, DatePipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderComponent} from "../header/header.component";
import { FlightService } from '../../services/flight.service';
import { ClienteService } from '../../services/cliente.service';


@Component({
  selector: 'app-confirm-reservation',
  standalone: true,
  imports: [
    DecimalPipe,
    FormsModule,
    HeaderComponent,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    CurrencyPipe,
    DatePipe
  ],
  templateUrl: './confirm-reservation.component.html',
  styleUrl: './confirm-reservation.component.css'
})
export class ConfirmReservationComponent {
  idVoo: number = 0;
  saldoMilhas: number = 0;
  quantidadePassagens: number = 1;
  precoPorPassagem: number = 1200; // valor fixo por enquanto
  valorFinal: number = this.precoPorPassagem;

  milhasNecessarias: number = this.valorFinal / 5;

  milhasUsadas: number = 0;
  descontoEmReais: number = 0;
  voo: any;

  constructor(private route: ActivatedRoute, protected flightService: FlightService, protected clienteService: ClienteService) {}

  ngOnInit() {
    this.idVoo = this.route.snapshot.params['idVoo'];
    const authToken = localStorage.getItem('token');
    if (!authToken) {
      console.error('Token não encontrado.');
      return;
    }

    this.flightService.buscarVooPorCodigo(this.idVoo, authToken).subscribe({
      next: (response) => {
        this.voo = response;
        this.precoPorPassagem = response.valor_passagem;
        this.atualizarValorFinal();
        this.updateSaldoMilhas();
      },
      error: (err) => {
        console.error('Erro ao buscar voo:', err);
      }
    });
  }

  atualizarValorFinal() {
    const total = this.quantidadePassagens * this.precoPorPassagem;
    this.milhasNecessarias = total / 5;
    this.descontoEmReais = (this.milhasUsadas || 0);
    this.valorFinal = total - this.descontoEmReais;

  }

  updateSaldoMilhas(): void {
    const userCodigo = localStorage.getItem('user_codigo');
    const token = localStorage.getItem('token');

    if (!userCodigo) {
      console.error('Codigo do usuário ou token não disponível.');
      return;
    }

    this.clienteService.getSaldoMilhas(userCodigo).subscribe({
      next: (response) => {
        this.saldoMilhas = response.saldoMilhas;
      },
      error: (err) => {
        console.error('Erro ao consultar saldo milhas:', err);
      },
    });
  }

}
