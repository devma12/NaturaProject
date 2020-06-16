import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HerbariumComponent } from './herbarium.component';

describe('HerbariumComponent', () => {
  let component: HerbariumComponent;
  let fixture: ComponentFixture<HerbariumComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HerbariumComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HerbariumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
