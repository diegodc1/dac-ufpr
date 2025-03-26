import { Component, OnInit } from '@angular/core';
import { ReservaService } from '../../services/reserva.service';  
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, HeaderComponent, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  saldoMilhas: number = 1000;  // Saldo de milhas fictício
  reservas: any[] = [];
  reservasFiltradas: any[] = [];
  mostrarAcoes: boolean = true;  // Variável para controlar a visibilidade da coluna Ações

  constructor(private reservaService: ReservaService) {}

  ngOnInit() {
    this.carregarReservas();
  }

  carregarReservas() {
    // Dados fictícios de reservas
    this.reservas = [
      {
        codigo: 'ABC123',
        dataHora: '2025-04-10 10:00',
        aeroportoOrigem: 'Aeroporto de São Paulo (GRU)',
        aeroportoDestino: 'Aeroporto do Rio de Janeiro (GIG)',
        estado: 'CRIADA',
      },
      {
        codigo: 'DEF456',
        dataHora: '2025-04-12 15:30',
        aeroportoOrigem: 'Aeroporto de Brasília (BSB)',
        aeroportoDestino: 'Aeroporto de Recife (REC)',
        estado: 'CHECK-IN',
      },
      {
        codigo: 'GHI789',
        dataHora: '2025-04-15 08:00',
        aeroportoOrigem: 'Aeroporto de Salvador (SSA)',
        aeroportoDestino: 'Aeroporto de Fortaleza (FOR)',
        estado: 'CANCELADA',
      },
      {
        codigo: 'JKL012',
        dataHora: '2025-04-20 18:00',
        aeroportoOrigem: 'Aeroporto de Porto Alegre (POA)',
        aeroportoDestino: 'Aeroporto de Curitiba (CWB)',
        estado: 'CRIADA',
      },
    ];
    
    this.reservasFiltradas = this.reservas;  // Inicializa com todas as reservas
  }

  mostrarReservasCriadas() {
    // Filtra apenas as reservas com estado "CRIADA"
    this.reservasFiltradas = this.reservas.filter(reserva => reserva.estado === 'CRIADA');
    this.mostrarAcoes = false; // Esconde a coluna Ações ao filtrar reservas "CRIADA"
  }

  verReserva(reserva: any) {
    // Aqui você pode implementar a lógica para ver a reserva
    console.log(reserva);
  }

  cancelarReserva(reserva: any) {
    // Verifica se a reserva é válida para cancelamento e cancela a reserva
    this.reservaService.cancelarReserva(reserva);
    this.carregarReservas();  // Atualiza a lista de reservas após o cancelamento
  }

  verReservas() {
    // Navegar para a tela de reservas (ou executar outra ação)
    console.log('Ver todas as reservas');
  }
}
