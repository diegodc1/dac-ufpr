import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from "../header/header.component"; 

interface Voo {
  origem: string;
  destino: string;
  dataHora: string;
  preco: number;
}

@Component({
  selector: 'app-make-reservation',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent], 
  templateUrl: './make-reservation.component.html',
  styleUrls: ['./make-reservation.component.css']
})
export class MakeReservationComponent {
  origem: string = '';
  destino: string = '';
  filteredFlights: Voo[] = [];

  voos: Voo[] = [
    { origem: 'São Paulo', destino: 'Nova York', dataHora: '29 Jan 2026, 14:50', preco: 3500 },
    { origem: 'Rio de Janeiro', destino: 'Paris', dataHora: '29 Jan 2026, 15:00', preco: 4000 },
    { origem: 'Mombasa', destino: 'Nairobi', dataHora: '29 Jan 2026, 14:30', preco: 12000 }
  ];

  buscarVoos() {
    this.filteredFlights = this.voos.filter(voo =>
      voo.origem.toLowerCase().includes(this.origem.toLowerCase()) &&
      voo.destino.toLowerCase().includes(this.destino.toLowerCase())
    );
  }

  selecionarVoo(voo: Voo) {
    alert(`Voo selecionado: ${voo.origem} para ${voo.destino}`);
  }
}