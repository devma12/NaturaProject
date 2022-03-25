import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Species } from 'src/app/models/species.model';

@Component({
  selector: 'app-new-entry',
  templateUrl: './new-entry.component.html',
  styleUrls: ['./new-entry.component.scss']
})
export class NewEntryComponent implements OnInit {

  @Input() type: string;
  @Input() species: Species[];

  @Output() entry = new EventEmitter<any>(); 

  entryForm: FormGroup;

  selectedFile: File;

  maxDate: Date = new Date();

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {

    this.entryForm = this.formBuilder.group({
      name: ['', Validators.required],
      file: ['', Validators.required],
      date: ['', Validators.required],
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

    const infos = {
      selectedFile: this.selectedFile,
      name: formValue['name'],
      date: formValue['date'],
      description: formValue['description'],
      location: formValue['location'],
      species: formValue['suggestion']
    }
    this.entry.emit(infos);
  }

}
