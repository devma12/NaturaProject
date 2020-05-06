import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-field-list-item',
  templateUrl: './field-list-item.component.html',
  styleUrls: ['./field-list-item.component.scss']
})
export class FieldListItemComponent implements OnInit {

  @Input() name: string;
  @Input() text: string;

  isLong: boolean = false;

  constructor() { }

  ngOnInit(): void {
    this.isLong = (this.text.length > 70);
  }

}
