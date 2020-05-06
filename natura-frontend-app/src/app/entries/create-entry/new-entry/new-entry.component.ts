import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Species } from 'src/app/models/species.model';

@Component({
  selector: 'app-new-entry',
  templateUrl: './new-entry.component.html',
  styleUrls: ['./new-entry.component.scss']
})
export class NewEntryComponent implements OnInit {

  @Input('type') type: string;
  @Input() species: Species[];

  @Output() entry = new EventEmitter<any>(); 

  entryForm: FormGroup;

  selectedFile: File;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {

    this.entryForm = this.formBuilder.group({
      name: ['', Validators.required],
      file: ['', Validators.required],
      description: [''],
      location: [''],
      suggestion: ['']
    });

  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  createEntry() {
    const formValue = this.entryForm.value;

    const species: Species = this.entryForm.controls['suggestion'].value;
    const specie = formValue['suggestion'];

    const infos = {
      selectedFile: this.selectedFile,
      name: formValue['name'],
      description: formValue['description'],
      location: formValue['location'],
      species: formValue['suggestion']
    }
    this.entry.emit(infos);
  }

}
