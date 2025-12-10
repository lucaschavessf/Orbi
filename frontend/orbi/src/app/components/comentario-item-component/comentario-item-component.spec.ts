import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComentarioItemComponent } from './comentario-item-component';

describe('ComentarioItemComponent', () => {
  let component: ComentarioItemComponent;
  let fixture: ComponentFixture<ComentarioItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComentarioItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComentarioItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
