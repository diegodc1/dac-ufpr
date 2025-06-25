import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClienteService } from '../../services/cliente.service';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';


registerLocaleData(localePt);

@Component({
  standalone:true,
  imports: [HeaderComponent, CommonModule, ],
  selector: 'app-ver-reserva',
  templateUrl: './ver-reserva.component.html',
  styleUrls: ['./ver-reserva.component.css']
})
export class VerReservaComponent implements OnInit {
  reserva: any = null;

  constructor(
    private route: ActivatedRoute,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    const codigoReserva = this.route.snapshot.paramMap.get('codigoReserva');
    if (codigoReserva) {
      this.carregarDetalhesReserva(codigoReserva);
    }
  }

  carregarDetalhesReserva(codigoReserva: string): void {
    this.clienteService.getDetalhesReserva(codigoReserva).subscribe({
      next: (reserva) => {
        this.reserva = reserva;
        console.log(this.reserva)
      },
      error: (err) => console.error('Erro ao carregar detalhes da reserva:', err),
    });
  }
}
