import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private authService: AuthService) {
   }

  ngOnInit(): void {
    this.authService.redirectUrl = this.route.snapshot.queryParams['redirectUrl'] || '/home';
    this.initForm();
  }

  initForm() {

    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmitForm() {
    const formValue = this.loginForm.value;
    this.authService.login(formValue['username'], formValue['password']).then(
      value => {
        console.log('authenticated !');
      }, error => {
        console.error('Failed to authenticate !');
      }
    );
  }

}
