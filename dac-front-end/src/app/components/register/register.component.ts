import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective,NgxMaskPipe, provideNgxMask  } from 'ngx-mask';
import { ViaCepResponse } from '../../interfaces/via-cep-response.interface';
// import {ViaCepService} from "../../services/via-cep.service";
import { HttpClientModule } from '@angular/common/http';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective, HttpClientModule],
  providers: [provideNgxMask()],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      street: ['', Validators.required],
      neighborhood: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      number: ['', [Validators.required, Validators.pattern(/^\d+$/)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      console.log(this.registerForm.value);
    } else {
      Object.keys(this.registerForm.controls).forEach(field => {
        const control = this.registerForm.get(field);
        control?.markAsTouched({ onlySelf: true });
      });
    }
  }

  // onCepChange(): void {
  //   const cep = this.registerForm.get('postalCode')?.value;
  //   if (cep && cep.length === 9) {
  //     this.viaCepService.getAddressByCep(cep.replace('-', '')).subscribe(
  //       (data:ViaCepResponse) => {
  //         if (data && !data.erro) {
  //           this.registerForm.patchValue({
  //             street: data.logradouro,
  //             neighborhood: data.bairro,
  //             city: data.localidade,
  //             state: data.uf
  //           });
  //         } else {
  //           alert('CEP não encontrado!');
  //         }
  //       },
  //       (error) => {
  //         console.error('Erro ao buscar o endereço: ', error);
  //       }
  //     );
  //   }
  // }
}
