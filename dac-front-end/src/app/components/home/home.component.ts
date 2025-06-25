import { Component, OnInit } from '@angular/core';
import { ClienteService } from '../../services/cliente.service';
import { Router, RouterModule } from '@angular/router';
import { format, addMinutes } from 'date-fns';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ModalCancelarReservaComponent } from '../modal-cancelar-reserva/modal-cancelar-reserva.component';
import { HeaderComponent } from '../header/header.component';
import { FlightService } from '../../services/flight.service';
import { HttpClientModule } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { ptBR } from 'date-fns/locale';

registerLocaleData(localePt);

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ModalCancelarReservaComponent, HeaderComponent, HttpClientModule],
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
  aeroportos: any[] = [];
  mostrarModal: boolean = false;
  reservaSelecionada: any = null;

  constructor(
    private clienteService: ClienteService,
    private router: Router,
    private flightService: FlightService  ) {}

  ngOnInit() {
    this.carregarTelaInicial();
     this.loadAeroportos();
  }

  carregarTelaInicial() {
    const userEmail = localStorage.getItem('user_email');
    const token = localStorage.getItem('token');
    const clienteId = localStorage.getItem('user_codigo');

       if (!clienteId || !token || !userEmail) {
      console.error('Dados do usuário (ID, email ou token) não disponíveis.');
      return;
    }


    this.clienteService.getTelaInicialCliente(clienteId).subscribe({
      next: (dados) => {
        this.saldoMilhas = dados.saldoMilhas;
        this.voos = dados.voos;

        this.reservas = dados.reservas
          .map((reserva: any) => {
            const data = new Date(reserva.data);
            return {
              ...reserva,
              dataFormatada: data.toLocaleString('pt-BR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                hour12: false
              })
            };
          })
          .sort((a: any, b: any) => new Date(a.voo.data).getTime() - new Date(b.voo.data).getTime());
        this.reservasFiltradas = this.reservas;
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
    if (reserva.estado === 'RESERVADO' || reserva.estado === 'CRIADA') {
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
        return 'CHECK-IN';
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

// comprarMilhas(valorEmReais: number) {
//   const clienteId = '123'; //pegar o id do cliente
//   this.clienteService.comprarMilhas(clienteId, valorEmReais).subscribe({
//       next: (response) => {
//           console.log('Milhas compradas com sucesso:', response);
//           alert(`Milhas compradas com sucesso! Novo saldo: ${response.saldoMilhas}`);
//       },
//       error: (err) => console.error('Erro ao comprar milhas:', err),
//   });
// }
private getAuthToken(): string | null {
    const authToken = localStorage.getItem('token');
    return authToken;
  }

 loadAeroportos(): void {
    const authToken = this.getAuthToken();
    console.log('HomeComponent: Tentando carregar aeroportos...')
    if (authToken) {
      this.flightService.getAeroportos(authToken).subscribe({
        next: (data: any[]) => {
          this.aeroportos = data;
          console.log('Aeroportos carregados na Home:', this.aeroportos);
        },
        error: (error: any) => {
          console.error('Erro ao carregar aeroportos na Home:', error);
        }
      });
    } else {
      console.error('Token de autenticação não encontrado para carregar aeroportos na Home.');
    }
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
