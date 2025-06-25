import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {CurrencyPipe, DatePipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderComponent} from "../header/header.component";
import { FlightService } from '../../services/flight.service';
import { ClienteService } from '../../services/cliente.service';
import { ReservaService } from '../../services/reserva.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';



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

  constructor(private route: ActivatedRoute, protected flightService: FlightService, protected clienteService: ClienteService, protected reservaService: ReservaService, private router: Router) {}

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

  makePayment(): void {
    const userCodigoStr = localStorage.getItem('user_codigo');
    const userCodigo = userCodigoStr ? Number(userCodigoStr) : null;

    if (userCodigo === null) {
      console.error('Código do usuário não disponível');
      return;
    }

    const novaReserva = {
      codigo_cliente: userCodigo,
      valor: this.valorFinal,
      milhas_utilizadas: this.milhasUsadas,
      quantidade_poltronas: this.quantidadePassagens,
      codigo_voo: this.idVoo
    };

    this.reservaService.criarReserva(novaReserva).subscribe({
      next: (resposta) => {
        Swal.fire({
          icon: 'success',
          title: 'Reserva criada!',
          text: 'Sua reserva foi criada com sucesso.',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['/home']);
        });
      },
      error: (erro) => {
        Swal.fire({
          icon: 'error',
          title: 'Erro',
          text: 'Não foi possível criar a reserva.',
          confirmButtonText: 'OK'
        });
      }
    });

  }
}
