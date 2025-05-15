import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {AuthService} from "../../services/auth.service";
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';

interface Login {
  email: string;
  senha: string;
  tipo: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    HttpClientModule
  ],
  providers: [AuthService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private router: Router, private authService: AuthService, private http: HttpClient) {}
  showLoginError = false;
  loginErrorMessage: string = '';

  loginData = {
    email: '',
    senha: ''
  };

  logins: Login[] = [
    { email: "funcionario@gmail.com", senha: '123', tipo: 'FUNCIONARIO'},
    { email: "cliente@gmail.com", senha: '123', tipo: 'CLIENTE'},
  ];

  login(form: NgForm) {
    this.loginErrorMessage = '';

    if (form.invalid) {
      this.loginErrorMessage = "Preencha todos os campos corretamente.";
      return;
    }

    const loginEncontrado = this.logins.find(
      user => user.email === this.loginData.email && user.senha === this.loginData.senha
    );

    if (loginEncontrado) {
      if (loginEncontrado.tipo === 'FUNCIONARIO') {
        this.router.navigate(['/home-employee']);
      } else if (loginEncontrado.tipo === 'CLIENTE') {
        this.router.navigate(['/home']);
      }
    } else {
      this.loginErrorMessage = "Email ou senha incorretos.";
    }
  }

  goToRegister() {
      this.router.navigate(['/register']); // Redireciona para a página de registro
    }
}
