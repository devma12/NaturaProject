import { Component, Input, OnInit } from '@angular/core';
import { Entry } from 'src/app/models/entries/entry.model';
import { Image } from 'src/app/models/image.model';

@Component({
  selector: 'app-entry-card',
  templateUrl: './entry-card.component.html',
  styleUrls: ['./entry-card.component.scss']
})
export class EntryCardComponent implements OnInit {

  @Input('entry') entry: Entry;

  picture: any = {};

  constructor() { }

  ngOnInit(): void {
    if (this.entry) {
      // get image
      console.log(this.entry.name);
      let image: Image = this.entry.image;
      if (image) {
        const base64Data = image.data;
        const type: string = image.type;
        this.picture = `data:${type};base64,${base64Data}`;
      }
    }
  }

}
