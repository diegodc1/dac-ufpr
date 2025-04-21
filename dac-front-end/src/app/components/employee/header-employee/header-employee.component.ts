import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-employee',
  standalone: true,
    imports: [
        RouterLink
    ],
  templateUrl: './header-employee.component.html',
  styleUrl: './header-employee.component.css'
})
export class HeaderEmployeeComponent {
  constructor(private router: Router){}

  logout(): void {
    this.router.navigate(['/login']);
  }
}
