import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { LoadingService } from './loading.service';
import { AlertService } from './alert.service';
import { SimpleSnackBar, MatSnackBarRef } from '@angular/material/snack-bar';

@Injectable()
export class LoadingFromServerService {
    
    errorAlert: MatSnackBarRef<SimpleSnackBar>;
    
    constructor(public loader: LoadingService,
                public alert: AlertService) { }
  
    loading() {
        this.resetErrorAlert();
        this.loader.startLoading();
    }
  
    loaded() {
        this.loader.stopLoading();
    }

    error(msg: string) {
        this.loader.stopLoading();
        this.openErrorAlert(msg);
    }

    reset() {
        this.resetErrorAlert();
        this.loader.stopLoading();
    }

    openErrorAlert(msg: string) {
        console.error(msg);
        this.errorAlert = this.alert.openAlert(msg, 'X', 'error');
    }

    private resetErrorAlert() {
        if (this.errorAlert) {
            this.errorAlert.dismiss();
            this.errorAlert = null;
        }
    }

}