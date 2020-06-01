import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';

@Injectable()
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