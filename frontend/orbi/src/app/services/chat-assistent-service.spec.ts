import { TestBed } from '@angular/core/testing';

import { ChatAssistentService } from './chat-assistent-service';

describe('ChatAssistentService', () => {
  let service: ChatAssistentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatAssistentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
