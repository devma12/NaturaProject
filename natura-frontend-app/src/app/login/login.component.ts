import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoadingService } from '../services/loading.service';

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
    private authService: AuthService,
    public loadingService: LoadingService) {
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
    this.loadingService.startLoading();
    const formValue = this.loginForm.value;
    this.authService.login(formValue['username'], formValue['password']).then(
      value => {
        this.loadingService.stopLoading();
        console.log('authenticated !');
      }, error => {
        console.error('Failed to authenticate !');
      }
    );
  }

}
