import { Component, OnInit } from '@angular/core';
import { Validators, FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { CustomErrorStateMatcher } from '../matchers/error-state.matcher';
import { AuthService } from '../services/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  
  matcher :CustomErrorStateMatcher;

  loginForm: FormGroup;
  email: FormControl;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private authService: AuthService) {
    this.matcher = new CustomErrorStateMatcher();
   }

  ngOnInit(): void {
    this.authService.redirectUrl = this.route.snapshot.queryParams['redirectUrl'] || '/home';
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
    this.authService.login(formValue['email'], formValue['password']).then(
      value => {
        console.log('authenticated !');
      }, error => {
        console.error('Failed to authenticate !');
      }
    );
  }

}