import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FlowerService } from 'src/app/services/flower.service';
import { Flower } from 'src/app/models/entries/flower.model';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
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

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService,
              private flowerService: FlowerService) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {

    this.entryForm = this.formBuilder.group({
      name: ['', Validators.required],
      file: ['', Validators.required],
      description: [''],
      location: [''],
      species: ['']
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
      description: formValue['description'],
      location: formValue['location'],
      species: formValue['species']
    }
    this.entry.emit(infos);
  }

}
