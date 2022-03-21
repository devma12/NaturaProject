import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { NewSpeciesComponent } from './new-species.component';

describe('NewSpeciesComponent', () => {
  let component: NewSpeciesComponent;
  let fixture: ComponentFixture<NewSpeciesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NewSpeciesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewSpeciesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
