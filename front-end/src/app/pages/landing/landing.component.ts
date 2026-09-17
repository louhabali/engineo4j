import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterLink],
  templateUrl: './landing.component.html',
})
export class LandingComponent {
  features = [
    { title: 'NEO4J GRAPH DATABASE', desc: 'Real-time collaborative filtering & high-speed relationship traversal.' },
    { title: 'SPRING BOOT & KAFKA & REST', desc: 'Microservices architecture connected via asynchronous event streams , rest apis.' },
    { title: 'ANGULAR & tailwind', desc: 'Responsive dark-mode UI optimized with custom Spider-Man visual aesthetics.' }
  ];

  partners = [
    { name: 'Neo4j' },
    { name: 'Spring Boot' },
    { name: 'Apache Kafka' },
    { name: 'Angular' },
    { name: 'Docker' }
  ];
}