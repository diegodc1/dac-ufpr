import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFlightModalComponent } from './create-flight-modal.component';

describe('CreateFlightModalComponent', () => {
  let component: CreateFlightModalComponent;
  let fixture: ComponentFixture<CreateFlightModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateFlightModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateFlightModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
