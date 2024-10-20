import { Injectable } from '@angular/core';
import { MatLegacySnackBarRef as MatSnackBarRef, LegacySimpleSnackBar as SimpleSnackBar } from '@angular/material/legacy-snack-bar';
import { AlertService } from './alert.service';
import { LoadingService } from './loading.service';

@Injectable({
    providedIn: 'root'
})
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