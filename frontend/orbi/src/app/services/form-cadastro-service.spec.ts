import { TestBed } from '@angular/core/testing';

import { FormCadastroService } from './form-cadastro-service';

describe('FormCadastroService', () => {
  let service: FormCadastroService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormCadastroService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
