import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSpeciesComponent } from './new-species.component';

describe('NewSpeciesComponent', () => {
  let component: NewSpeciesComponent;
  let fixture: ComponentFixture<NewSpeciesComponent>;

  beforeEach(async(() => {
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
