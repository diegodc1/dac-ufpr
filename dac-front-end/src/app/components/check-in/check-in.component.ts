import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-check-in',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './check-in.component.html',
  styleUrl: './check-in.component.css'
})
export class CheckInComponent {

}
