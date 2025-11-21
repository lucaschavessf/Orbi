import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostComentarioComponent } from './post-comentario-component';

describe('PostComentarioComponent', () => {
  let component: PostComentarioComponent;
  let fixture: ComponentFixture<PostComentarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostComentarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostComentarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
