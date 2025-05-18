import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  formBuilder = inject(FormBuilder);
  authService = inject(AuthService);

  constructor() {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: new FormControl('', [Validators.required, Validators.minLength(7), Validators.maxLength(20)]),
      password: new FormControl('', [Validators.minLength(8), Validators.maxLength(20)]),
      passwordConfirmation: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const { username, password, passwordConfirmation } = this.registerForm.value;

      if (username && password && passwordConfirmation) {
        this.authService.register(username, password, passwordConfirmation)
          .subscribe({
            next: () => {},
            error: (err) => {}
          });
      }
    }
  }
}
