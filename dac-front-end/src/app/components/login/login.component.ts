import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm, ReactiveFormsModule } from "@angular/forms";
import { NgIf } from "@angular/common";
import { AuthService } from "../../services/auth.service";
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { LoginData } from '../../models/login/login-data.model';
import { LoginService } from '../../services/login/login.service';

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

  loginData: LoginData = new LoginData();

  constructor(
    private router: Router,
    private loginService: LoginService
  ) { }

  showLoginError = false;
  loginErrorMessage: string = '';

  fazerLogin(form: NgForm) {

    this.loginErrorMessage = '';

    if (form.invalid) {
      this.loginErrorMessage = "Preencha todos os campos corretamente.";
      return;
    }

    let observable = this.loginService.login(this.loginData);

    observable.subscribe(

      (usuario) => {

        if (usuario != null) {
          console.log(usuario);
          this.loginService.usuarioLogado = usuario; //alocando usuario (logado) na LS

          if (usuario.tipo == "FUNCIONARIO") {
            this.router.navigate(["/home-employee/" + usuario.userId]);
          }

        }
        else {
          this.loginErrorMessage = "Email ou senha incorretos.";
        }
      });
  }

  // constructor(private router: Router, private authService: AuthService, private http: HttpClient) {}
  // showLoginError = false;
  // loginErrorMessage: string = '';

  // loginData = {
  //   email: '',
  //   senha: ''
  // };

  // logins: Login[] = [
  //   { email: "funcionario@gmail.com", senha: '123', tipo: 'FUNCIONARIO'},
  //   { email: "cliente@gmail.com", senha: '123', tipo: 'CLIENTE'},
  // ];

  // login(form: NgForm) {
  //   this.loginErrorMessage = '';

  //   if (form.invalid) {
  //     Swal.fire({
  //       icon: 'warning',
  //       title: 'Campos obrigatórios',
  //       text: 'Preencha todos os campos corretamente.'
  //     });
  //     return;
  //   }

  //   Swal.fire({
  //     title: 'Entrando...',
  //     text: 'Verificando suas credenciais.',
  //     allowOutsideClick: false,
  //     didOpen: () => {
  //       Swal.showLoading();
  //     }
  //   });

  //   this.http.post('http://localhost:3000/auth/login', {
  //     login: this.loginData.email,
  //     senha: this.loginData.senha
  //   }).subscribe({
  //     next: (response: any) => {
  //       localStorage.setItem('token', response.access_token);
  //       localStorage.setItem('usuario', JSON.stringify(response.usuario));

  //       Swal.fire({
  //         icon: 'success',
  //         title: 'Login realizado!',
  //         text: `Bem-vindo(a), ${response.usuario.nome}!`,
  //         confirmButtonText: 'Continuar'
  //       }).then(() => {
  //         const tipoUsuario = response.usuario.tipo;
  //         if (tipoUsuario === 'FUNCIONARIO') {
  //           this.router.navigate(['/home-employee']);
  //         } else {
  //           this.router.navigate(['/home']);
  //         }
  //       });
  //     },
  //     error: (err) => {
  //       if (err.status === 401) {
  //         Swal.fire({
  //           icon: 'error',
  //           title: 'Login inválido',
  //           text: 'Email ou senha incorretos.'
  //         });
  //       } else {
  //         Swal.fire({
  //           icon: 'error',
  //           title: 'Erro ao fazer login',
  //           text: err.error?.message || 'Tente novamente mais tarde.'
  //         });
  //       }
  //     }
  //   });
  // }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}
