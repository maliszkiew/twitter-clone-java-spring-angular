import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { DialogService } from '../../services/dialog.service';
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  formBuilder = inject(FormBuilder);
  authService = inject(AuthService);
  dialogService = inject(DialogService);
  router = inject(Router);

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]),
      password: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]),
      passwordConfirmation: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]),
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmation = form.get('passwordConfirmation')?.value;
    return password === confirmation ? null : { mismatch: true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const { username, password, passwordConfirmation } = this.registerForm.value;

      this.authService.register(username, password, passwordConfirmation)
        .subscribe({
          next: () => {
            this.dialogService.openDialog(true, 'Registration successful');
            this.registerForm.reset();
            this.router.navigate(['/login']);
          },
          error: (err) => {
            this.dialogService.openDialog(false, `Registration failed: ${err.error || 'Please try again'}`);
          }
        });
    }
  }
}
