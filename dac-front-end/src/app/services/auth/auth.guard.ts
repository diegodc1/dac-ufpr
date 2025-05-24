//import { CanActivateFn } from '@angular/router';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../login/login.service';
import { Router } from '@angular/router';

// export const authGuard: CanActivateFn = (route, state) => {
//   return true;
// };

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private loginService: LoginService,
    private router: Router
  ) { }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {


    const usuarioLogado = this.loginService.usuarioLogado; // getter

    let url = state.url;

    if (usuarioLogado) { // se o usuario está alocado no Local Storage...

      if (route.data?.['role'] && route.data?.['role'].indexOf(usuarioLogado.tipo) === -1) {
        // Se o perfil do usuário não está no perfil da rota
        // direciona para o login
        this.router.navigate(['/login'],
          { queryParams: { error: "Acesso proibido a " + url } });
        return false;
      }
      // em qualquer outro caso, permite o acesso
      return true;
    }
    // Se não está logado, vai para login
    this.router.navigate(['/login'],
      { queryParams: { error: "Deve fazer o login antes de acessar " + url } });
    return false;
  }
}