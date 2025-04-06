import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderComponent} from "../header/header.component";

@Component({
  selector: 'app-confirm-reservation',
  standalone: true,
  imports: [
    DecimalPipe,
    FormsModule,
    HeaderComponent,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './confirm-reservation.component.html',
  styleUrl: './confirm-reservation.component.css'
})
export class ConfirmReservationComponent {
  idVoo: number = 0;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.idVoo = this.route.snapshot.params['idVoo'];
    console.log(this.idVoo);
  }
}
