import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ClienteService } from '../../services/cliente.service';


interface MileageTransaction {
  date: string;
  reservationCode: string | null;
  amountReais: number; 
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
  
  private readonly API_GATEWAY_URL = ''; 


  constructor(private http : HttpClient, private clienteService: ClienteService){}
  ngOnInit(): void {
     this.getMileageExtract();
  }

getMileageExtract(): void {
    this.http.get<MileageTransaction[]>(this.API_GATEWAY_URL).subscribe({
      next: (data) => {
        this.mileageExtract = data.map(item => ({
          ...item,
          type: this.determineTransactionType(item.description) 
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

    const clienteId = '123'; //pegar o id do cliente
    const valorEmReais = this.totalPrice;

    this.clienteService.comprarMilhas(clienteId, valorEmReais).subscribe({
      next: (response) => {
        alert(`Compra realizada com sucesso! Novo saldo de milhas: ${response.saldoMilhas}`);
        this.getMileageExtract(); 
      },
      error: (err) => {
        console.error('Erro ao comprar milhas:', err);
        alert('Erro ao realizar a compra de milhas. Tente novamente mais tarde.');
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
        amountReais: 100,
        miles: 20,
        description: 'COMPRA DE MILHAS',
        type: 'ENTRADA',
      },
    ];
  }
}
