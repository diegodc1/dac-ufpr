import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { ClienteService } from '../../services/cliente.service';
import {HttpHeaders} from "@angular/common/http";
import Swal from "sweetalert2";


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  saldoMilhas: number = 0;

  constructor(private router: Router, private clienteService: ClienteService){}

  ngOnInit(): void {
    this.updateSaldoMilhas();
  }

  updateSaldoMilhas(): void {
    const userCodigo = localStorage.getItem('user_codigo');
    const token = localStorage.getItem('token');

    if (!userCodigo) {
      console.error('Codigo do usuário ou token não disponível.');
      return;
    }

    this.clienteService.getSaldoMilhas(userCodigo).subscribe({
      next: (response) => {
        this.saldoMilhas = response.saldoMilhas;
      },
      error: (err) => {
        console.error('Erro ao consultar saldo milhas:', err);
      },
    });
  }

  logout(): void {
    this.router.navigate(['/login']);
  }
}
