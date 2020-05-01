import { Component, Input, OnInit } from '@angular/core';
import { Entry } from 'src/app/models/entries/entry.model';
import { Image } from 'src/app/models/image.model';
import { EntryUtils } from '../entry.utils';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-entry-card',
  templateUrl: './entry-card.component.html',
  styleUrls: ['./entry-card.component.scss']
})
export class EntryCardComponent implements OnInit {

  @Input('entry') entry: Entry;

  picture: any = {};

  constructor(private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.picture = EntryUtils.getEntryPictureBase64Data(this.entry);
  }

  openView() {
    this.router.navigate(['/entries/view', this.entry.id]);
  }

}
