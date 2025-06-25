import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BackendReservationWithFlight, ReservaService } from '../../services/reserva.service';
import { Usuario } from '../../models/usuario/usuario.model';
import { LoginService } from '../../services/login/login.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-check-in',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './check-in.component.html',
  styleUrl: './check-in.component.css',
  providers: [DatePipe],
})
export class CheckInComponent implements OnInit {

  flightsForCheckin: BackendReservationWithFlight[] = [];
  isLoading: boolean = true;
  errorMessage: string | null = null;



  constructor( private reservaService: ReservaService, private datePipe: DatePipe, private loginService: LoginService, private router: Router){}

  ngOnInit(): void {
         this.loadReservationsForCheckin();
  }
  get usuarioLogado(): Usuario | null {
      return this.loginService.usuarioLogado;
    }


  loadReservationsForCheckin(){
    this.isLoading = true;
    this.errorMessage = null;

    const clienteId = localStorage.getItem('user_codigo');
     if (!clienteId) {
      this.errorMessage = 'Erro: Código do cliente não encontrado. Por favor, faça login novamente.';
      this.isLoading = false;
      console.error('CheckInComponent: Código do cliente não encontrado.');
           return;
    }

    this.reservaService.getReservationsFromBackend(clienteId).subscribe({
      next:(reservations) => {
         const safeReservations = Array.isArray(reservations) ? reservations : [];
        const now = new Date();
        const forttEightHouresLater = new Date(now.getTime() + 48 *60*60* 1000);

        console.log('--- Debug de Datas para Check-in ---');
        console.log('Data/Hora Atual (Frontend):', now.toLocaleString('pt-BR', { timeZoneName: 'short' }));
        console.log('48 Horas no Futuro (Frontend):', forttEightHouresLater.toLocaleString('pt-BR', { timeZoneName: 'short' }));
        console.log('Reservas recebidas do Backend (bruto, após conversão segura):', safeReservations);

       this.flightsForCheckin = reservations.filter((reserva) => {
          if (!reserva.voo || !reserva.voo.data) {
              console.warn(`Reserva ${reserva.codigo} será ignorada: dados de voo ou data de voo inválidos/ausentes.`);
            return false;
          }


              const flightDepartureDateTime = new Date(reserva.voo.data);
              const isUpcomingAndWithin48Hours =
              flightDepartureDateTime > now &&
              flightDepartureDateTime <= forttEightHouresLater;
              console.log(reserva)
            const isReservationEligible = reserva.estado === "CRIADA" || reserva.estado === "CONFIRMADO" ;


          console.log(`--- Análise Reserva ${reserva.codigo} (Voo ${reserva.voo.codigo}) ---`);
          console.log('  Data/Hora do Voo (Backend String):', reserva.voo.data);
          console.log('  Convertido para Date (Frontend):', flightDepartureDateTime.toLocaleString('pt-BR', { timeZoneName: 'short' }));
          console.log('  Está no futuro e dentro das 48h?', isUpcomingAndWithin48Hours);
          console.log('  Estado da Reserva:', reserva.estado, 'É elegível (CRIADA)?', isReservationEligible);
          console.log('  Resultado da Filtragem para esta Reserva:', isUpcomingAndWithin48Hours && isReservationEligible);
          return isUpcomingAndWithin48Hours && isReservationEligible;
        });
         if (this.flightsForCheckin.length === 0) {
          console.log('Nenhum voo disponível para check-in nas próximas 48 horas após a filtragem.');
          this.errorMessage = 'Não há voos disponíveis para check-in no momento.';
        } else {
          this.errorMessage = null;
        }
           this.isLoading = false;
      },error:(err) =>{
        console.log('Erro ao carregar reservas  para check-in: ', err);
         this.errorMessage = 'Não foi possível carregar os voos disponíveis para check-in. Tente novamente mais tarde.';
        this.isLoading = false;
      }
    })
  }

  realizarCheckin(reserva : BackendReservationWithFlight){
    const novoEstado = 'CHECK-IN';


      this.reservaService.updateReservationStatusOnBackend(reserva.codigo, novoEstado).subscribe({
        next:(response)=> {
        console.log('Check-in realizado com sucesso!', response);
            const confirmacao = confirm(`Check-in para o voo ${reserva.voo.codigo} da reserva ${reserva.codigo} realizado com sucesso!\n\nDeseja ir para a tela inicial?`);
        if (confirmacao) {
        this.router.navigate(['/home']);
        } else {
          this.loadReservationsForCheckin();
        }        }, error: (err) =>{
          console.error('Erro ao realizar check-in: ', err)
          this.errorMessage = `Falha ao realizar check-in para a reserva ${reserva.codigo}. Erro: ${err.message}`;

        }
      })
  }
}
