import { Injectable } from '@angular/core';
import { MatLegacySnackBar as MatSnackBar, MatLegacySnackBarHorizontalPosition as MatSnackBarHorizontalPosition, MatLegacySnackBarVerticalPosition as MatSnackBarVerticalPosition, MatLegacySnackBarRef as MatSnackBarRef, LegacySimpleSnackBar as SimpleSnackBar } from '@angular/material/legacy-snack-bar';

@Injectable({
    providedIn: 'root'
})
export class AlertService {

    horizontalPosition: MatSnackBarHorizontalPosition = 'end';
    verticalPosition: MatSnackBarVerticalPosition = 'bottom';

    constructor(private snackBar: MatSnackBar) { }

    openAlert(message: string, action: string, type: string): MatSnackBarRef<SimpleSnackBar> {

        let snackBarRef = this.openSnackBar(message, action, `alert-${type}`, 100000, 'end', 'bottom');

        snackBarRef.onAction().subscribe(() => {
            snackBarRef.dismiss();
        });

        return snackBarRef;
    }

    openSnackBar(message: string,
        action: string,
        className: string,
        durationMs: number,
        horizontalPosition: MatSnackBarHorizontalPosition,
        verticalPosition: MatSnackBarVerticalPosition): MatSnackBarRef<SimpleSnackBar> {

        let snackBarRef = this.snackBar.open(message, action, {
            duration: durationMs,
            horizontalPosition: horizontalPosition,
            verticalPosition: verticalPosition,
            panelClass: [className]
        });

        return snackBarRef;
    }

}