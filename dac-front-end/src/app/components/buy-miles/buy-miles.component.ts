import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ClienteService } from '../../services/cliente.service';
import Swal from 'sweetalert2';



interface MileageTransaction {
  date: string;
  reservationCode: string | null;
  amountReais: string;
  miles: number;
  description: string;
  type: 'ENTRADA' | 'SAÍDA';
}

@Component({
  selector: 'app-buy-miles',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent, HttpClientModule],
  templateUrl: './buy-miles.component.html',
  styleUrl: './buy-miles.component.css'
})
export class BuyMilesComponent implements OnInit {
  milesToBuy: number = 0;
  totalPrice: number = 0;
  mileageExtract: MileageTransaction[] = [];

  private readonly API_GATEWAY_URL = 'http://localhost:3000';


  constructor(private http : HttpClient, private clienteService: ClienteService){}
  ngOnInit(): void {
     this.getMileageExtract();
  }

// getMileageExtract(): void {
//     this.http.get<MileageTransaction[]>(this.API_GATEWAY_URL).subscribe({
//       next: (data) => {
//         this.mileageExtract = data.map(item => ({
//           ...item,
//           type: this.determineTransactionType(item.description)
//         }));
//       },
//       error: (error) => {
//         console.error('Erro ao buscar extrato de milhas:', error);
//       }
//     });
//   }


    getMileageExtract(): void {

        const userCodigo = localStorage.getItem('user_codigo');
        const token = localStorage.getItem('token');

        if (!userCodigo || !token) {
          console.error('Codigo do usuário ou token não disponível.');
          return;
        }

        const headers = new HttpHeaders({
          'Authorization': `Bearer ${token}`
        });

      this.http.get<any>(`${this.API_GATEWAY_URL}/clientes/${userCodigo}/milhas`, { headers }).subscribe({
        next: (data) => {
          const transacoes = data.transacoes;

          this.mileageExtract = transacoes.map((item: any) => ({
            date: item.data,
            reservationCode: item.codigo_reserva,
            amountReais: item.valor_reais,
            miles: item.quantidade_milhas,
            description: item.descricao,
            type: this.determineTransactionType(item.descricao)
          }));
        },
        error: (error) => {
          console.error('Erro ao buscar extrato de milhas:', error);
        }
      });
    }


    private determineTransactionType(description: string): 'ENTRADA' | 'SAÍDA' {
      if (description.includes('COMPRA DE MILHAS')) {
        return 'ENTRADA';
      }
      if (description.includes('->')) {
        return 'SAÍDA';
      }
      return 'ENTRADA';
    }

   // Atualiza o valor total com base na quantidade de milhas
   onMilesInputChange(event: any): void {
    const miles = event.target.value;
    this.milesToBuy = miles ? parseInt(miles, 10) : 0;
    this.totalPrice = this.milesToBuy * 5;
  }

  // Realiza a compra de milhas
  buyMiles(): void {
    if (this.milesToBuy <= 0) {
      alert('Por favor, insira uma quantidade válida de milhas para comprar.');
      return;
    }


    const loggedUserStr = localStorage.getItem('usuario');
    if (!loggedUserStr) {
      alert('Usuário não está logado.');
      return;
    }
    const loggedUser = JSON.parse(loggedUserStr);
    const clienteId = loggedUser.codigo;

    const quantidade = this.milesToBuy;
    const valorPago = this.totalPrice;

    this.clienteService.comprarMilhas(clienteId, quantidade, valorPago).subscribe({
      next: (response) => {
        Swal.fire({
          icon: 'success',
          title: 'Compra realizada com sucesso!',
          text: `Novo saldo de milhas: ${response.saldo_milhas}`,
          confirmButtonColor: '#3085d6',
        });

        this.getMileageExtract();
      },
      error: (err) => {
        console.error('Erro ao comprar milhas:', err);
        Swal.fire({
          icon: 'error',
          title: 'Erro ao comprar milhas',
          text: 'Tente novamente mais tarde.',
          confirmButtonColor: '#d33',
        });
      },
    });
  }

  // Carrega o extrato de transações de milhas
  carregarExtratoMilhas(): void {
    // teste para simular o extrato
    this.mileageExtract = [
      {
        date: '2025-05-25',
        reservationCode: null,
        amountReais: '100',
        miles: 20,
        description: 'COMPRA DE MILHAS',
        type: 'ENTRADA',
      },
    ];
  }
}
