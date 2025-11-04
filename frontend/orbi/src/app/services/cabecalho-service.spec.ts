import { TestBed } from '@angular/core/testing';

import { CabecalhoService } from './cabecalho-service';

describe('CabecalhoService', () => {
  let service: CabecalhoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CabecalhoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
