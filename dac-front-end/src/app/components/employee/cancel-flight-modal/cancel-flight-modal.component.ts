import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cancel-flight-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cancel-flight-modal.component.html',
  styleUrls: ['./cancel-flight-modal.component.css']
})
export class CancelFlightModalComponent {
  @Input() voo: any; 
  @Output() confirmCancelFlight = new EventEmitter<void>();
  @Output() closeModalEvent = new EventEmitter<void>(); 

  confirmCancel() {
    this.confirmCancelFlight.emit(); 
  }

  closeModal() {
    this.closeModalEvent.emit();
  }
}
