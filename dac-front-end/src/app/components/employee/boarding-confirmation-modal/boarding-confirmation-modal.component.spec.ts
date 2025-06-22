import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardingConfirmationModalComponent } from './boarding-confirmation-modal.component';

describe('BoardingConfirmationModalComponent', () => {
  let component: BoardingConfirmationModalComponent;
  let fixture: ComponentFixture<BoardingConfirmationModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoardingConfirmationModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BoardingConfirmationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
