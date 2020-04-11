import { Component, OnInit } from '@angular/core';
import { Validators, FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { CustomErrorStateMatcher } from '../matchers/error-state.matcher';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  
  matcher :CustomErrorStateMatcher;

  loginForm: FormGroup;
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

    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });

    this.email = this.loginForm.controls['email'] as FormControl;
  }

  onSubmitForm() {
    const formValue = this.loginForm.value;
    this.authService.authenticate(formValue['email'], formValue['password']);
  }

}
