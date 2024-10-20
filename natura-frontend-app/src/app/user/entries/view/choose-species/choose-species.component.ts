import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Species } from 'src/app/core/models/species.model';

@Component({
  selector: 'app-choose-species',
  templateUrl: './choose-species.component.html',
  styleUrls: ['./choose-species.component.scss']
})
export class ChooseSpeciesComponent implements OnInit {

  selected: Species;
  selectControl = new UntypedFormControl();
  options: Species[];
  filteredOptions: Observable<Species[]>;

  constructor(public dialogRef: MatDialogRef<ChooseSpeciesComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Species[]) {
      this.options = data;
     }

    ngOnInit() {
      this.filteredOptions = this.selectControl.valueChanges
        .pipe(
          startWith(''),
          map(value => this._filter(value))
        );
    }
  
    private _filter(value: string): Species[] {
      const filterValue = value.toLowerCase();
  
      return this.options.filter(option => option.commonName.toLowerCase().includes(filterValue));
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

  updateSelected(value: Species) {
    this.selected = value;
  }

  getOptionName(option: Species) {
    if (option)
      return option.commonName;
    else
      return null;
  }

}
