import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthRangeSliderComponent } from './month-range-slider.component';

describe('MonthRangeSliderComponent', () => {
  let component: MonthRangeSliderComponent;
  let fixture: ComponentFixture<MonthRangeSliderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonthRangeSliderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonthRangeSliderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
