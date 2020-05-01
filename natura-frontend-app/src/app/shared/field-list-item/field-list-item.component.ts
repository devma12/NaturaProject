import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-field-list-item',
  templateUrl: './field-list-item.component.html',
  styleUrls: ['./field-list-item.component.scss']
})
export class FieldListItemComponent implements OnInit {

  @Input() title: string;
  @Input() text: string;

  constructor() { }

  ngOnInit(): void {
  }

}
