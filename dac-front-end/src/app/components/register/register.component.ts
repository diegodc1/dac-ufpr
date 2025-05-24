import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective,NgxMaskPipe, provideNgxMask  } from 'ngx-mask';
import { ViaCepResponse } from '../../interfaces/via-cep-response.interface';
import {ViaCepService} from "../../services/via-cep.service";
import { HttpClientModule } from '@angular/common/http';
import { ClienteService } from '../../services/cliente.service'
import Swal from 'sweetalert2';
import { Router } from '@angular/router';



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

  constructor(private fb: FormBuilder, private viaCepService: ViaCepService, private clienteService: ClienteService, private router: Router) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      street: ['', Validators.required],
      neighborhood: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      number: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
      complemento: [''],
      saldoMilhas: [0, [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const formValue = this.registerForm.value;

      const cadastroClienteDTO = {
        cpf: formValue.cpf,
        email: formValue.email,
        nome: formValue.fullName,
        saldo_milhas: formValue.saldoMilhas,
        endereco: {
          cep: formValue.postalCode,
          uf: formValue.state,
          cidade: formValue.city,
          bairro: formValue.neighborhood,
          rua: formValue.street,
          numero: formValue.number,
          complemento: formValue.complemento
        }
      };


      Swal.fire({
        title: 'Realizando seu cadastro...',
        text: 'Por favor, aguarde.',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      this.clienteService.cadastrarCliente(cadastroClienteDTO).subscribe({
        next: (res) => {
          Swal.fire({
            icon: 'success',
            title: 'Cadastro realizado!',
            text: 'Sua senha foi enviada para o e-mail informado.',
            confirmButtonText: 'Ir para o login'
          }).then(() => {
            this.router.navigate(['/login']);
          });
          this.registerForm.reset();
        },
        error: (err) => {
          if (err.status === 409) {
            Swal.fire({
              icon: 'warning',
              title: 'Usuário já existe!',
              text: 'O e-mail ou CPF informado já está cadastrado.',
            });
          } else {
            Swal.fire({
              icon: 'error',
              title: 'Erro ao cadastrar',
              text: err.error?.message || 'Tente novamente mais tarde.'
            });
          }
        }
      });

    } else {
      Object.keys(this.registerForm.controls).forEach(field => {
        const control = this.registerForm.get(field);
        control?.markAsTouched({ onlySelf: true });
      });
    }
  }


  onCepChange(): void {
    const cep = this.registerForm.get('postalCode')?.value;

    if (cep && cep.length === 8) {
      this.viaCepService.getAddressByCep(cep.replace('-', '')).subscribe(
        (data:ViaCepResponse) => {
          if (data && !data.erro) {
            this.registerForm.patchValue({
              street: data.logradouro,
              neighborhood: data.bairro,
              city: data.localidade,
              state: data.uf
            });
          } else {
            alert('CEP não encontrado!');
          }
        },
        (error) => {
          console.error('Erro ao buscar o endereço: ', error);
        }
      );
    }
  }
}
