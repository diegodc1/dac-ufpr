import { Routes } from '@angular/router';
import {RegisterComponent} from "./components/register/register.component";
import { LoginComponent } from './components/login/login.component';
import { MakeReservationComponent } from './components/make-reservation/make-reservation.component';
import { VerReservaComponent } from './components/ver-reserva/ver-reserva.component';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import {ConfirmReservationComponent} from "./components/confirm-reservation/confirm-reservation.component";
import { BuyMilesComponent } from './components/buy-miles/buy-miles.component';
import { CheckInComponent } from './components/check-in/check-in.component';
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
    component: LoginComponent
  },
  {
    path: 'make-reservation',
    component: MakeReservationComponent
  },
  {
    path: 'confirm-reservation/:idVoo',
    component: ConfirmReservationComponent
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
  },
  {
    path:'buy-miles',
    component: BuyMilesComponent
  },
  {
    path:'check-in',
    component:CheckInComponent
  }
];
