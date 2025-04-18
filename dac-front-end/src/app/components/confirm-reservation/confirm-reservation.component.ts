import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {CurrencyPipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderComponent} from "../header/header.component";

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
    CurrencyPipe
  ],
  templateUrl: './confirm-reservation.component.html',
  styleUrl: './confirm-reservation.component.css'
})
export class ConfirmReservationComponent {
  idVoo: number = 0;
  quantidadePassagens: number = 1;
  precoPorPassagem: number = 1200; // valor fixo por enquanto
  valorFinal: number = this.precoPorPassagem;

  milhasNecessarias: number = this.valorFinal / 5;
  saldoMilhasUsuario: number = 5000;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.idVoo = this.route.snapshot.params['idVoo'];
    console.log(this.idVoo);
  }



  atualizarValorFinal() {
    this.valorFinal = this.quantidadePassagens * this.precoPorPassagem;
    this.milhasNecessarias = this.valorFinal / 5;
  }
}
