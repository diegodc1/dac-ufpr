import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-flight-completion-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './flight-completion-modal.component.html',
  styleUrls: ['./flight-completion-modal.component.css']
})
export class FlightCompletionModalComponent {
  @Input() voo: any; 
  @Output() confirmFlightCompletion = new EventEmitter<void>();
  @Output() closeModalEvent = new EventEmitter<void>(); 

  confirmCompletion() {
    this.confirmFlightCompletion.emit(); 
  }

  closeModal() {
    this.closeModalEvent.emit(); 
  }
}
