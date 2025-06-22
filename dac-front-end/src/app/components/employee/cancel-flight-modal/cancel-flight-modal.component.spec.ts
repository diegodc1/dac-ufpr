import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelFlightModalComponent } from './cancel-flight-modal.component';

describe('CancelFlightModalComponent', () => {
  let component: CancelFlightModalComponent;
  let fixture: ComponentFixture<CancelFlightModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CancelFlightModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CancelFlightModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
