import { TestBed } from '@angular/core/testing';

import { HighchartsControllerService } from './highcharts-controller.service';

describe('HighchartsControllerService', () => {
  let service: HighchartsControllerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HighchartsControllerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
