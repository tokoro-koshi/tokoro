export interface Testimonial {
  id: string;
  userId: string;
  rating: number; // 1 to 5
  message: string;
  createdAt: string; // ISO date string
}
