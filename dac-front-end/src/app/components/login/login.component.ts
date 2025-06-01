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

  constructor(
    private router: Router,
    private loginService: LoginService,
    private authService: AuthService,
    private http: HttpClient
  ) { }
  showLoginError = false;
  loginErrorMessage: string = '';

  loginData = {
    login: '',
    senha: ''
  };

  login(form: NgForm) {
    this.loginErrorMessage = '';

    if (form.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos obrigatórios',
        text: 'Preencha todos os campos corretamente.'
      });
      return;
    }

    Swal.fire({
      title: 'Entrando...',
      text: 'Verificando suas credenciais.',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.http.post('http://localhost:3000/login', {
      login: this.loginData.login,
      senha: this.loginData.senha
    }).subscribe({
      next: (response: any) => {

        if (response.tipo == 'CLIENTE') {
          localStorage.setItem('token', response.access_token);
          localStorage.setItem('usuario', JSON.stringify(response.usuario));
          localStorage.setItem('user_email', response.usuario.email);

          Swal.fire({
            icon: 'success',
            title: 'Login realizado!',
            text: `Bem-vindo(a), ${response.usuario.nome}!`,
            confirmButtonText: 'Continuar'
          }).then(() => {
            this.router.navigate(['/home']);
            // const tipoUsuario = response.usuario.tipo;
            // if (tipoUsuario === 'FUNCIONARIO') {

            // } else {
            //   this.router.navigate(['/home']);
            // }
          });
        } else if (response.tipo == 'FUNCIONARIO') {
          Swal.fire({
            icon: 'success',
            title: 'Login realizado!',
            text: `Bem-vindo(a), ${response.name}!`,
            confirmButtonText: 'Continuar'
          }).then(() => {
            this.loginService.usuarioLogado = response;
            this.router.navigate(['/home-employee/' + response.userId]);
          });
        }

      },
      error: (err) => {
        if (err.status === 401) {
          Swal.fire({
            icon: 'error',
            title: 'Login inválido',
            text: 'Email ou senha incorretos.'
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Erro ao fazer login',
            text: err.error?.message || 'Tente novamente mais tarde.'
          });
        }
      }
    });
  }



  // fazerLogin(form: NgForm) {

  //   this.loginErrorMessage = '';

  //   if (form.invalid) {
  //     this.loginErrorMessage = "Preencha todos os campos corretamente.";
  //     return;
  //   }

  //   let observable = this.loginService.login(this.loginData);

  //   observable.subscribe(

  //     (usuario) => {

  //       if (usuario != null) {
  //         console.log(usuario);
  //         this.loginService.usuarioLogado = usuario; //alocando usuario (logado) na LS

  //         if (usuario.tipo == "FUNCIONARIO") {
  //           this.router.navigate(["/home-employee/" + usuario.userId]);
  //         }

  //       }
  //       else {
  //         this.loginErrorMessage = "Email ou senha incorretos.";
  //       }
  //     });
  //   }



  goToRegister() {
    this.router.navigate(['/register']);
  }
}
