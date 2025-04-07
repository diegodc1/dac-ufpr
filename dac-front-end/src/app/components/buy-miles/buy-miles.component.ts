import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-buy-miles',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './buy-miles.component.html',
  styleUrl: './buy-miles.component.css'
})
export class BuyMilesComponent {

}
