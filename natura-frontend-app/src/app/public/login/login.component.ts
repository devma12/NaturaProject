import { Component, OnInit, OnDestroy } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoadingFromServerService } from 'src/app/core/services/loading-from-server.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: UntypedFormGroup;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    public loadingService: LoadingFromServerService) {
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
    this.loadingService.loading();
    const formValue = this.loginForm.value;
    this.authService.login(formValue['username'], formValue['password']).then(
      value => {
        this.loadingService.loaded();
        console.log('authenticated !');
      }, error => {
        let msg = 'Failed to authenticate !';
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
