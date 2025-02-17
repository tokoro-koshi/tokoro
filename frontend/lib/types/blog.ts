export interface Comment {
  id: string;
  value: string;
  createdAt: string; // ISO 8601 date-time string
  updatedAt: string; // ISO 8601 date-time string
}

export interface Blog {
  id: string;
  title: string;
  content: string;
  authorId: string;
  tags: string[];
  comments: Comment[];
  createdAt: string; // ISO 8601 date-time string
  updatedAt: string; // ISO 8601 date-time string
}
