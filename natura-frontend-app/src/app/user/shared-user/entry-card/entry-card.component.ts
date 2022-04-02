import { Component, Input, OnInit } from '@angular/core';
import { Entry } from 'src/app/core/models/entries/entry.model';
import { Image } from 'src/app/core/models/image.model';
import { EntryUtils } from '../entry.utils';
import { ActivatedRoute, Router } from '@angular/router';
import { SpeciesType } from 'src/app/core/models/type.enum';

@Component({
  selector: 'app-entry-card',
  templateUrl: './entry-card.component.html',
  styleUrls: ['./entry-card.component.scss']
})
export class EntryCardComponent implements OnInit {

  @Input() entry: Entry;
  @Input() type: SpeciesType;

  picture: any = {};

  constructor(private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.picture = EntryUtils.getEntryPictureBase64Data(this.entry);
  }

}
