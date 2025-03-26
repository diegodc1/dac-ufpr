import { Routes } from '@angular/router';
import {RegisterComponent} from "./components/register/register.component";
import { LoginComponent } from './components/login/login.component';
import { HeaderComponent } from './components/header/header.component';
import { VerReservaComponent } from './components/ver-reserva/ver-reserva.component';
import { HomeComponent } from './components/home/home.component';
export const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },

  { path: 'header',
    component: HeaderComponent,

  },
  {
    path: 'ver-reserva/:codigoReserva',
    component: VerReservaComponent,

  },
  {
    path: 'home',
    component: HomeComponent,
  }
];
