import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing/landing.component';
import { RegisterComponent } from './pages/auth/register/register.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { ProfileComponent } from './pages/auth/profile/profile.component';
import { CatalogComponent } from './pages/catalog/catalog.component';

export const routes: Routes = [
    {
    path: '',
    component: LandingComponent,
  },
  {
    path :'register',
    component: RegisterComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path :'profile',
    component : ProfileComponent, 
  },
  {
    path :'catalog',
    component : CatalogComponent,
  }
];
