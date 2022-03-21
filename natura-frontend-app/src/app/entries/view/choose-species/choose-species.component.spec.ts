import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ChooseSpeciesComponent } from './choose-species.component';

describe('ChooseSpeciesComponent', () => {
  let component: ChooseSpeciesComponent;
  let fixture: ComponentFixture<ChooseSpeciesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ChooseSpeciesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseSpeciesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
