import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; // Para acessar parâmetros da URL
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ver-reserva',
  standalone: true,
  templateUrl: './ver-reserva.component.html',
  styleUrls: ['./ver-reserva.component.css'],
  imports:[ CommonModule]
})
export class VerReservaComponent implements OnInit {
  reserva: any = {}; // Objeto para armazenar os dados da reserva

  // Dados fake de reserva para simulação
  reservasFake: any[] = [
    {
      codigo: 'ABC123',
      dataHora: '2025-04-10 10:00',
      aeroportoOrigem: 'Aeroporto de São Paulo (GRU)',
      aeroportoDestino: 'Aeroporto do Rio de Janeiro (GIG)',
      valorGasto: 500.00,
      milhasGastadas: 3000,
      estado: 'CRIADA',
    },
    {
      codigo: 'DEF456',
      dataHora: '2025-04-12 15:30',
      aeroportoOrigem: 'Aeroporto de Brasília (BSB)',
      aeroportoDestino: 'Aeroporto de Recife (REC)',
      valorGasto: 800.00,
      milhasGastadas: 4000,
      estado: 'CHECK-IN',
    },
    // Outras reservas fake
  ];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Pega o código da reserva da URL
    const codigoReserva = this.route.snapshot.paramMap.get('codigo');
    
    // Busca os dados da reserva fake com base no código
    this.reserva = this.reservasFake.find(reserva => reserva.codigo === codigoReserva);
  }
}
