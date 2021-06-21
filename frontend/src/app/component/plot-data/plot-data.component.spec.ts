import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlotDataComponent } from './plot-data.component';

describe('PlotDataComponent', () => {
  let component: PlotDataComponent;
  let fixture: ComponentFixture<PlotDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlotDataComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlotDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
