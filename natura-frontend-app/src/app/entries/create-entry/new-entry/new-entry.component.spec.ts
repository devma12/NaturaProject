import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { NewEntryComponent } from './new-entry.component';

describe('NewEntryComponent', () => {
  let component: NewEntryComponent;
  let fixture: ComponentFixture<NewEntryComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NewEntryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
