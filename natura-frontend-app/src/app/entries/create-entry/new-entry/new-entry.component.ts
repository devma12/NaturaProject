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
      species: ['']
    });

  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  createEntry() {
    // Get given name
    const formValue = this.entryForm.value;
    const name: string = formValue['name'];

    const infos = {
      selectedFile: this.selectedFile,
      name: name
    }
    this.entry.emit(infos);
  }

  // createEntry() {
  //   // Create Form Data to send picture
  //   const uploadImageData = new FormData();
  //   uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);

  //   // Get given name
  //   const formValue = this.entryForm.value;
  //   const name: string = formValue['name'];

  //   // Get logged user to be set as creator
  //   const user = this.authService.user.getValue();

  //   // Create new flower entry
  //   this.flowerService.create(uploadImageData, name, user).subscribe(
  //     data => {
  //       this.router.navigate(['/home']);
  //     },
  //     error => {
  //       console.log('Failed to create new Flower entry.');
  //     }
  //   );
  // }

}
