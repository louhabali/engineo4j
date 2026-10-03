import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing/landing.component';
import { RegisterComponent } from './pages/auth/register/register.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { ProfileComponent } from './pages/auth/profile/profile.component';
import { CatalogComponent } from './pages/catalog/catalog.component';
import { RecommendationsComponent } from './pages/recommendations/recommendations.component';
import { ErrorComponent } from './shared/components/error/error.component';
import { GraphComponent } from './pages/graph/graph.component';
import { MovieDetailComponent } from './pages/movie-detail/movie-detail.component';
import { authGuard, guestGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: LandingComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [guestGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [guestGuard],
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard],
  },
  {
    path: 'catalog',
    component: CatalogComponent,
    canActivate: [authGuard],
  },
  {
    path: 'recommendations',
    component: RecommendationsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'graph',
    component: GraphComponent,
  },
  { path: 'movie/:id', component: MovieDetailComponent },
  { path: '401', component: ErrorComponent, data: { code: '401' } },
  { path: '429', component: ErrorComponent, data: { code: '429' } },
  { path: '403', component: ErrorComponent, data: { code: '403' } },
  { path: '500', component: ErrorComponent, data: { code: '500' } },
  { path: '404', component: ErrorComponent, data: { code: '404' } },
  { path: '400', component: ErrorComponent, data: { code: '400' } },

  // WILDCARD REDIRECT TO 404
  { path: '**', redirectTo: '404' }
];
