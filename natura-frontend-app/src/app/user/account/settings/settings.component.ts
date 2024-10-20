import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatLegacyDialog as MatDialog } from '@angular/material/legacy-dialog';
import { Subscription } from 'rxjs';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { LoadingFromServerService } from 'src/app/core/services/loading-from-server.service';
import { UserService } from 'src/app/core/services/user.service';
import { ChangeEmailComponent } from './change-email/change-email.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { AlertService } from 'src/app/core/services/alert.service';
import { MatLegacySnackBarRef as MatSnackBarRef, LegacySimpleSnackBar as SimpleSnackBar } from '@angular/material/legacy-snack-bar';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, OnDestroy {

  user: User;
  userSubscription: Subscription;

  infoPopup: MatSnackBarRef<SimpleSnackBar>;

  constructor(private authService: AuthService,
    private userService: UserService,
    public dialog: MatDialog,
    public alertService: AlertService,
    public loadingService: LoadingFromServerService) { }

  ngOnInit(): void {
    this.userSubscription = this.authService.user.subscribe(data => {
      this.user = data;
    });
  }

  changeEmail() {
    // open popup to enable user to enter new email address
    const dialogRef = this.dialog.open(ChangeEmailComponent, {
      width: '400px',
      data: this.user.email
    });

    // when popup is closed
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadingService.loading();
        const email: string = result;
        this.userService.changeEmail(this.user, email).subscribe(
          data => {
            this.authService.user.next(data);
            this.loadingService.loaded();
            this.infoPopup = this.alertService.openAlert('Email address is correctly changed.', 'X', 'info');
          },
          error => {
            let msg: string = 'Failed to change email !';
            if (error.status === 401 || error.status === 404)
                  msg = error.error.message;
            this.loadingService.error(msg);
          }
        );
      }
    });

  }

  changePassword() {
    // open popup to enable user to enter new email address
    const dialogRef = this.dialog.open(ChangePasswordComponent, {
      width: '400px'
    });

    // when popup is closed
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadingService.loading();
        const oldPassword: string = result.old;
        const newPassword: string = result.new;
        this.userService.changePassword(this.user, oldPassword, newPassword).subscribe(
          data => {
            this.authService.user.next(data);
            this.loadingService.loaded();
            this.infoPopup = this.alertService.openAlert('Password is correctly changed.', 'X', 'info');
          },
          error => {
            let msg: string = 'Failed to change password !';
            if (error.status === 401 || error.status === 404)
                  msg = error.error.message;
            this.loadingService.error(msg);
          }
        );
      }
    });
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
    this.loadingService.reset();
    if (this.infoPopup)
      this.infoPopup.dismiss();
  }

}
