import apiClient from "@/lib/helpers/apiClient";
import { PlaceReview } from "@/lib/types/place-review";
import { UserClient } from '@/lib/requests/user.client';
import { Pagination } from '@/lib/types/pagination';

export class PlaceReviewClient {
  static async getPlaceReviewById(id: string): Promise<PlaceReview> {
    const response = await apiClient.get<PlaceReview>(`/reviews/${id}`);
    return response.data;
  }

  static async updatePlaceReview(id: string, review: PlaceReview): Promise<PlaceReview> {
    const response = await apiClient.put<PlaceReview>(`/reviews/${id}`, review);
    return response.data;
  }

  static async deletePlaceReview(id: string): Promise<void> {
    await apiClient.delete(`/reviews/${id}`);
  }

  static async getAllPlaceReviews(): Promise<PlaceReview[]> {
    const response = await apiClient.get<{payload:PlaceReview[]}>("/reviews");
    return response.data.payload;
  }

  static async savePlaceReview(review: PlaceReview): Promise<PlaceReview> {
    const response = await apiClient.post<PlaceReview>("/reviews", review);
    return response.data;
  }

  static async getUserPlaceReviews(userId: string): Promise<PlaceReview[]> {
    const response = await apiClient.get<Pagination<PlaceReview>>(`/reviews/user/${userId}`);
    return response.data.payload;
  }

  static async getPlaceReviews(placeId: string): Promise<PlaceReview[]> {
    const response = await apiClient.get<Pagination<PlaceReview>>(`/reviews/place/${placeId}`);
    const reviews = response.data.payload;
    
    for (const review of reviews) {
      const user = await UserClient.getUserDetails(review.userId);
      if (!user) continue;
      review.userName = user.name;
      review.userAvatar = user.picture;
    }

    return reviews;
  }
}
