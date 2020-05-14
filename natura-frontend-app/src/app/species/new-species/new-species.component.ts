import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Phenology } from 'src/app/models/phenology.model';
import { Species } from 'src/app/models/species.model';
import { SpeciesType } from 'src/app/models/type.enum';
import { SpeciesService } from 'src/app/services/species.service';

@Component({
  selector: 'app-new-species',
  templateUrl: './new-species.component.html',
  styleUrls: ['./new-species.component.scss']
})
export class NewSpeciesComponent implements OnInit {

  public SpeciesType = SpeciesType;

  speciesForm: FormGroup;

  ranges: any[] = [{start: 1, end: 1}];

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private speciesService: SpeciesService) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {

    this.speciesForm = this.formBuilder.group({
      type: ['', Validators.required],
      order: ['', Validators.required],
      family: ['', Validators.required],
      scientificName: ['', Validators.required],
      commonName: ['', Validators.required],
      habitatType: ['']
    });

  }

  addPhenology() {
    this.ranges.push({start:1, end:1});
  }

  removePhenology(range: any) {
    const index: number = this.ranges.indexOf(range);
    if (index !== -1) {
        this.ranges.splice(index, 1);
    } 
  }

  createSpecies() {
    const formValue = this.speciesForm.value;

    const species = new Species(formValue['commonName'], formValue['scientificName'], formValue['type']);
    species.order = formValue['order'];
    species.family = formValue['family'];
    species.habitatType = formValue['habitatType'];
    
    this.ranges.forEach(r => {
      if (r.start && r.end && r.start !== r.end) {
        const phenology = new Phenology(r.start, r.end);
        species.phenologies.push(phenology);
      }
    });

    this.speciesService.create(species).subscribe(
      data => {
        this.router.navigate(['/species/list']);
      },
      error => {
        console.error('Failed to create species');
      }
    );


  }

}
