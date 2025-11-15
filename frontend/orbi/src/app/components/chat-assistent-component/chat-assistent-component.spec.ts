import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatAssistentComponent } from './chat-assistent-component';

describe('ChatAssistentComponent', () => {
  let component: ChatAssistentComponent;
  let fixture: ComponentFixture<ChatAssistentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatAssistentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatAssistentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
