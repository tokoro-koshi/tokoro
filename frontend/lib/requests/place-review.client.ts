import apiClient from "@/lib/helpers/apiClient";
import { PlaceReview } from "@/lib/types/place-review";

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
    const response = await apiClient.get<{payload:PlaceReview[]}>(`/reviews/user/${userId}`);
    return response.data.payload;
  }

  static async getPlacePlaceReviews(placeId: string): Promise<PlaceReview[]> {
    const response = await apiClient.get<{payload:PlaceReview[]}>(`/reviews/place/${placeId}`);
    
    
    
    return response.data.payload;
  }
}
