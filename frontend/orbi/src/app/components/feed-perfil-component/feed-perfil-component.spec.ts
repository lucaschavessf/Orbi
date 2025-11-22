import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedPerfilComponent } from './feed-perfil-component';

describe('FeedPerfilComponent', () => {
  let component: FeedPerfilComponent;
  let fixture: ComponentFixture<FeedPerfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedPerfilComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedPerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
