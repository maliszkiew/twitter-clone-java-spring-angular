// dialog.service.ts
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/components/dialog/dialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  constructor(private dialog: MatDialog) {}

  openDialog(isSuccess: boolean, message: string) {
    this.dialog.open(DialogComponent, {
      width: '400px',
      data: {
        title: isSuccess ? 'Success' : 'Error',
        message: message,
        isSuccess: isSuccess
      }
    });
  }
}
