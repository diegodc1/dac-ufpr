import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
@Component({
  standalone:true,
  imports: [HeaderComponent, CommonModule],
  selector: 'app-ver-reserva',
  templateUrl: './ver-reserva.component.html',
  styleUrls: ['./ver-reserva.component.css']
})
export class VerReservaComponent implements OnInit {
  reserva: any;

  ngOnInit(): void {
    // Simulando dados de reserva
    this.reserva = {
      codigo: 'AB123',
      dataHora: '2025-03-26 15:30',
      aeroportoOrigem: 'Aeroporto Internacional de São Paulo',
      aeroportoDestino: 'Aeroporto do Rio de Janeiro',
      valorGasto: 500,
      milhasGastadas: 1500,
      estado: 'CRIADA'
    };
  }
}
