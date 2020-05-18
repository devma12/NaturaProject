import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldListItemComponent } from './field-list-item.component';

describe('FieldComponent', () => {
  let component: FieldListItemComponent;
  let fixture: ComponentFixture<FieldListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FieldListItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
