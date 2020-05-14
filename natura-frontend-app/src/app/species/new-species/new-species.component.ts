import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SpeciesType } from 'src/app/models/type.enum';
import { Month } from 'src/app/models/month.enum';
import { SpeciesService } from 'src/app/services/species.service';
import { Species } from 'src/app/models/species.model';
import { Router } from '@angular/router';
import { Phenology } from 'src/app/models/phenology.model';

@Component({
  selector: 'app-new-species',
  templateUrl: './new-species.component.html',
  styleUrls: ['./new-species.component.scss']
})
export class NewSpeciesComponent implements OnInit {

  public SpeciesType = SpeciesType;
  public Month = Month;

  speciesForm: FormGroup;

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
      start: [''],
      end: [''],
      habitatType: ['']
    });

  }

  createSpecies() {
    const formValue = this.speciesForm.value;

    const species = new Species(formValue['commonName'], formValue['scientificName'], formValue['type']);
    species.order = formValue['order'];
    species.family = formValue['family'];
    species.habitatType = formValue['habitatType'];
    
    const start = formValue['start'] ? formValue['start'] : null;
    const end = formValue['end'] ? formValue['end'] : null;
    if (start && end) {
      const phenology = new Phenology(start, end);
      species.phenologies.push(phenology);
    }

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
