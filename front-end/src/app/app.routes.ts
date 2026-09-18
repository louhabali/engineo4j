import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing/landing.component';
import { RegisterComponent } from './pages/auth/register/register.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { ProfileComponent } from './pages/auth/profile/profile.component';
import { CatalogComponent } from './pages/catalog/catalog.component';
import { RecommendationsComponent } from './pages/recommendations/recommendations.component';
import { ErrorComponent } from './shared/components/error/error.component';
import { GraphComponent } from './pages/graph/graph.component';

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
  },
  {
    path :'recommendations',
    component : RecommendationsComponent, 
  },
  {
    path :'graph',
    component : GraphComponent,
  },
  { path: '401', component: ErrorComponent, data: { code: '401' } },
  { path: '403', component: ErrorComponent, data: { code: '403' } },
  { path: '500', component: ErrorComponent, data: { code: '500' } },
  { path: '404', component: ErrorComponent, data: { code: '404' } },
  
  // WILDCARD REDIRECT TO 404
  { path: '**', redirectTo: '404' }
];
