export interface GraphNode {
  id: string;
  label: string;
  type: 'movie' | 'genre' | 'user';
  connections: string[];
}