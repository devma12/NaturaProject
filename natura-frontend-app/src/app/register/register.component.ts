import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomErrorStateMatcher } from '../matchers/error-state.matcher';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  matcher: CustomErrorStateMatcher;

  registerForm: FormGroup;
  email: FormControl;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService) {
    this.matcher = new CustomErrorStateMatcher();
   }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {

    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.email = this.registerForm.controls['email'] as FormControl;
  }

  onSubmitForm() {
    const formValue = this.registerForm.value;
    this.authService.register(formValue['email'], formValue['username'], formValue['password']).then(
      value => {
        console.log('registered !');
      }, error => {
        console.error('Failed to register !');
      }
    );
  }

}
