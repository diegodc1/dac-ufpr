import { Component, OnInit } from '@angular/core';
import { ClienteService } from '../../services/cliente.service';
import { Router, RouterModule } from '@angular/router';
import { format, addMinutes } from 'date-fns';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ModalCancelarReservaComponent } from '../modal-cancelar-reserva/modal-cancelar-reserva.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ModalCancelarReservaComponent, HeaderComponent], 
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  saldoMilhas: number = 0;
  reservas: any[] = [];
  reservasFiltradas: any[] = [];
  voos: any[] = [];
  mostrarAcoes: boolean = true;

  origem: string = '';
  destino: string = '';

  mostrarModal: boolean = false;
  reservaSelecionada: any = null;

  constructor(
    private clienteService: ClienteService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarTelaInicial();
  }

  carregarTelaInicial() {
    const clienteId = '123'; // pegar o id do cliente 
    this.clienteService.getTelaInicialCliente(clienteId).subscribe({
      next: (dados) => {
        this.saldoMilhas = dados.saldoMilhas;
        this.reservas = dados.reservas;
        this.reservasFiltradas = dados.reservas;
        this.voos = dados.voos;
      },
      error: (err) => console.error('Erro ao carregar tela inicial:', err),
    });
  }

  calcularHoraChegada(dataHora: string, horas: number, minutos: number): string {
    const dataPartida = new Date(dataHora);
    const chegada = addMinutes(dataPartida, horas * 60 + minutos);
    return format(chegada, 'HH:mm');
  }

  cancelarReserva(reserva: any) {
    if (reserva.estado === 'RESERVADO') {
      this.reservaSelecionada = reserva;
      this.mostrarModal = true;
    }
  }

  confirmarCancelamento() {
    if (this.reservaSelecionada) {
      this.clienteService
        .cancelarReserva(this.reservaSelecionada.codigo)
        .subscribe({
          next: () => {
            this.reservaSelecionada.estado = 'CANCELADO';
            this.mostrarModal = false;

            const index = this.reservas.findIndex(
              (r) => r.codigo === this.reservaSelecionada.codigo
            );
            if (index !== -1) {
              this.reservas[index].estado = 'CANCELADO';
            }
          },
          error: (err) =>
            console.error('Erro ao cancelar reserva:', err),
        });
    }
  }

  fecharModal() {
    this.mostrarModal = false;
  }

  getStatusTexto(estado: string): string {
    switch (estado) {
      case 'RESERVADO':
        return 'RESERVADO';
      case 'CHECK-IN':
        return 'REALIZADO';
      case 'CANCELADO':
        return 'CANCELADO';
      default:
        return estado;
    }
  }

  mostrarDetalhesReserva(codigoReserva: string) {
    this.clienteService.getDetalhesReserva(codigoReserva).subscribe({
        next: (reserva) => {
            console.log('Detalhes da Reserva:', reserva);
            alert(`
                Código: ${reserva.codigo}
                Data/Hora: ${reserva.dataHora}
                Origem: ${reserva.aeroportoOrigem}
                Destino: ${reserva.aeroportoDestino}
                Valor Gasto: R$ ${reserva.valorGasto}
                Milhas Gastas: ${reserva.milhasGastas}
                Estado: ${reserva.estado}
            `);
        },
        error: (err) => console.error('Erro ao obter detalhes da reserva:', err),
    });
}

comprarMilhas(valorEmReais: number) {
  const clienteId = '123'; //pegar o id do cliente
  this.clienteService.comprarMilhas(clienteId, valorEmReais).subscribe({
      next: (response) => {
          console.log('Milhas compradas com sucesso:', response);
          alert(`Milhas compradas com sucesso! Novo saldo: ${response.saldoMilhas}`);
      },
      error: (err) => console.error('Erro ao comprar milhas:', err),
  });
}

  buscarVoos() {
    this.router.navigate(['/make-reservation'], {
      queryParams: {
        origem: this.origem,
        destino: this.destino,
      },
    });
  }
}
