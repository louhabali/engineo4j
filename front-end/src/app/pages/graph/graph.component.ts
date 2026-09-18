import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

export interface GraphNode {
  id: string;
  label: string;
  type: 'movie' | 'genre' | 'user';
  connections: string[];
}

@Component({
  selector: 'app-graph-explorer',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './graph.component.html'
})
export class GraphComponent {
  selectedNode: GraphNode = {
    id: 'm1',
    label: 'Spider-Man: Across the Spider-Verse',
    type: 'movie',
    connections: ['Animation', 'Sci-Fi', 'Miles Morales', '99% Match Node']
  };

  cypherQuery = `MATCH (u:User {id: 'session_user'})-[r:RATED]->(m:Movie)
MATCH (m)-[:HAS_GENRE]->(g:Genre)<-[:HAS_GENRE]-(rec:Movie)
WHERE NOT (u)-[:RATED]->(rec)
RETURN rec, count(*) AS affinityScore
ORDER BY affinityScore DESC LIMIT 10;`;

  nodes: GraphNode[] = [
    { id: 'm1', label: 'Spider-Man: Spider-Verse', type: 'movie', connections: ['Animation', 'Sci-Fi'] },
    { id: 'g1', label: 'Sci-Fi Genre', type: 'genre', connections: ['Interstellar', 'The Matrix'] },
    { id: 'm2', label: 'Interstellar', type: 'movie', connections: ['Sci-Fi', 'Drama'] },
    { id: 'u1', label: 'User Node (You)', type: 'user', connections: ['Spider-Man', 'Interstellar'] }
  ];

  selectNode(node: GraphNode): void {
    this.selectedNode = node;
  }
}