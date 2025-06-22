import { Component } from '@angular/core';
import { RouterLink } from "@angular/router";
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login/login.service';
import { Usuario } from '../../../models/usuario/usuario.model';

@Component({
  selector: 'app-header-employee',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './header-employee.component.html',
  styleUrl: './header-employee.component.css'
})
export class HeaderEmployeeComponent {

  constructor(
    private router: Router,
    private loginService: LoginService
  ) { }

  get usuarioLogado(): Usuario | null {
    return this.loginService.usuarioLogado;
  }

  logout(): void {
    this.router.navigate(['/login']);
  }
}
