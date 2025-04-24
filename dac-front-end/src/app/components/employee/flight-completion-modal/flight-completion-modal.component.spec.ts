import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightCompletionModalComponent } from './flight-completion-modal.component';

describe('FlightCompletionModalComponent', () => {
  let component: FlightCompletionModalComponent;
  let fixture: ComponentFixture<FlightCompletionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FlightCompletionModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FlightCompletionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
