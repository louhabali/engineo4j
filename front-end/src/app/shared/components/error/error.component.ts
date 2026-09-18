import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

export interface ErrorConfig {
  code: string;
  title: string;
  description: string;
}

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './error.component.html'
})
export class ErrorComponent implements OnInit {
  errorCode = '404';

  errorConfigs: Record<string, ErrorConfig> = {
    '401': { code: '401', title: 'AUTHENTICATION REQUIRED', description: 'Your session JWT token is missing or expired. Authenticate to access this node.' },
    '403': { code: '403', title: 'NODE ACCESS FORBIDDEN', description: 'You do not have sufficient permissions to traverse this graph resource.' },
    '404': { code: '404', title: 'NODE NOT FOUND', description: 'The requested graph traversal route or movie node does not exist in the database.' },
    '500': { code: '500', title: 'GRAPH ENGINE FAILURE', description: 'An unexpected internal error occurred while executing the Neo4j query.' }
  };

  currentError: ErrorConfig = this.errorConfigs['404'];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    const codeFromRoute = this.route.snapshot.data['code'] || this.route.snapshot.paramMap.get('code');
    if (codeFromRoute && this.errorConfigs[codeFromRoute]) {
      this.errorCode = codeFromRoute;
      this.currentError = this.errorConfigs[codeFromRoute];
    }
  }
}