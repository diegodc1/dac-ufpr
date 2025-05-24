import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';


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
   milesToBuy: number | null = null;
  totalPrice: string = 'R$0,00';
  mileageExtract: MileageTransaction[] = [];
  
  private readonly API_GATEWAY_URL = ''; 


  constructor(private http : HttpClient){}
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

   onMilesInputChange(): void {
    if (this.milesToBuy !== null && this.milesToBuy > 0) {
      const pricePerThousandMiles = 5;
      const total = (this.milesToBuy / 1000) * pricePerThousandMiles;
      this.totalPrice = `R$${total.toFixed(2).replace('.', ',')}`;
    } else {
      this.totalPrice = 'R$0,00';
    }
  }

  buyMiles(): void {
    if (this.milesToBuy && this.milesToBuy > 0) {
      console.log(`Comprando ${this.milesToBuy} milhas por ${this.totalPrice}`);
       this.getMileageExtract();
    } else {
      alert('Por favor, insira uma quantidade de milhas válida para comprar.');
    }
  }
}
