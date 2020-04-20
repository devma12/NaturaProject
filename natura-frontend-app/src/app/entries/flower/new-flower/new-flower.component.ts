import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FlowerService } from 'src/app/services/flower.service';
import { Flower } from 'src/app/models/entries/flower.model';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-flower',
  templateUrl: './new-flower.component.html',
  styleUrls: ['./new-flower.component.scss']
})
export class NewFlowerComponent implements OnInit {

  entryForm: FormGroup;

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
      species: ['']
    });

  }

  createEntry() {
    const formValue = this.entryForm.value;
    const flower = new Flower(formValue['name']);
    this.flowerService.create(flower, this.authService.user.getValue()).subscribe(
      data => {
        this.router.navigate(['/home']);
      },
      error => {
        console.log('Failed to create new Flower entry.');
      }
    );
  }

}
