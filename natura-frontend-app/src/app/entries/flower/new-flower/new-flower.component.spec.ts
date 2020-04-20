import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewFlowerComponent } from './new-flower.component';

describe('NewFlowerComponent', () => {
  let component: NewFlowerComponent;
  let fixture: ComponentFixture<NewFlowerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewFlowerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewFlowerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
