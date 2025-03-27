import { Routes } from '@angular/router';
import {RegisterComponent} from "./components/register/register.component";
import { LoginComponent } from './components/login/login.component';
import { MakeReservationComponent } from './components/make-reservation/make-reservation.component';

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
  }
];
