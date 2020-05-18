import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ChangeContext, LabelType, Options } from 'ng5-slider';
import { Month } from 'src/app/models/month.enum';
import { EnumToArrayPipe } from 'src/app/pipes/enum-to-array.pipe';

@Component({
  selector: 'app-month-range-slider',
  templateUrl: './month-range-slider.component.html',
  styleUrls: ['./month-range-slider.component.scss']
})
export class MonthRangeSliderComponent implements OnInit {

  @Input() start: number = 1;

  @Output() startChange: EventEmitter<number> = new EventEmitter<number>();

  @Input() end: number = 1;

  @Output() endChange: EventEmitter<number> = new EventEmitter<number>();

  options: Options = {
    floor: 1,
    ceil: 12,
    step: 1,
    showTicks: true,
    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          return '<b>Start:</b> ' + this.enumToArrayPipe.getKey(Month, value);
        case LabelType.High:
          return '<b>End:</b> ' + this.enumToArrayPipe.getKey(Month, value);
        default:
          return this.enumToArrayPipe.getKey(Month, value);
      }
    }
  };

  constructor(private enumToArrayPipe: EnumToArrayPipe) { }

  ngOnInit(): void {
  }

  onRangeChange(changeContext: ChangeContext): void {
    this.startChange.emit(this.start);
    this.endChange.emit(this.end);
  }

}
