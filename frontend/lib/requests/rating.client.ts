import apiClient from '@/lib/helpers/apiClient';
import { Rating } from '@/lib/types/rating';

export class RatingClient {
  static async getRatingById(id: string): Promise<Rating> {
    const response = await apiClient.get<Rating>(`/ratings/${id}`);
    return response.data;
  }

  static async updateRating(id: string, rating: Rating): Promise<Rating> {
    const response = await apiClient.put<Rating>(`/ratings/${id}`, rating);
    return response.data;
  }

  static async deleteRating(id: string): Promise<void> {
    await apiClient.delete(`/ratings/${id}`);
  }

  static async getAllRatings(): Promise<Rating[]> {
    const response = await apiClient.get<Rating[]>(`/ratings`);
    return response.data;
  }

  static async createRating(rating: Rating): Promise<Rating> {
    const response = await apiClient.post<Rating>(`/ratings`, rating);
    return response.data;
  }
}
