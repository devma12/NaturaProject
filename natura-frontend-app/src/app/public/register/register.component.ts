import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomErrorStateMatcher } from 'src/app/core/matchers/error-state.matcher';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoadingFromServerService } from 'src/app/core/services/loading-from-server.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {

  matcher: CustomErrorStateMatcher;

  registerForm: FormGroup;
  email: FormControl;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    public loadingService: LoadingFromServerService) {
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
    this.loadingService.loading();
    const formValue = this.registerForm.value;
    this.authService.register(formValue['email'], formValue['username'], formValue['password']).then(
      value => {
        this.loadingService.loaded();
        console.log('Registered !');
      }, error => {
        let msg = 'Failed to register !';
        if (error.status === 401 && error.error && error.error.message) {
          msg = msg + '\n' + error.error.message;
        }
        this.loadingService.error(msg);
      }
    );
  }

  ngOnDestroy(): void {
    this.loadingService.reset();
  }

}
