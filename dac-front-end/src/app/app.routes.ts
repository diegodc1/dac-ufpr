// giy pullimport { Routes } from '@angular/router';
import { RegisterComponent } from "./components/register/register.component";
import { LoginComponent } from './components/login/login.component';

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

    component: HeaderComponent,

  },
  {
    path: 'ver-reserva/:codigoReserva',
    component: VerReservaComponent,

  },
  {
    path: 'home',
    component: HomeComponent,
<
  }
];
