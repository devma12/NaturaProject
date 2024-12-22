import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { Species } from 'src/app/core/models/species.model';

@Component({
    selector: 'app-new-entry',
    templateUrl: './new-entry.component.html',
    styleUrls: ['./new-entry.component.scss'],
    standalone: false
})
export class NewEntryComponent implements OnInit {
  @Input() type: string;
  @Input() species: Species[];

  @Output() entry = new EventEmitter<any>();

  entryForm: UntypedFormGroup;

  selectedFile: File;

  maxDate: Date = new Date();

  constructor(private formBuilder: UntypedFormBuilder) {}

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
      suggestion: [''],
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      console.log(file);
      this.selectedFile = file;
    }
  }

  createEntry() {
    const formValue = this.entryForm.value;

    const infos = {
      selectedFile: this.selectedFile,
      name: formValue['name'],
      date: formValue['date'],
      description: formValue['description'],
      location: formValue['location'],
      species: formValue['suggestion'],
    };
    this.entry.emit(infos);
  }
}
