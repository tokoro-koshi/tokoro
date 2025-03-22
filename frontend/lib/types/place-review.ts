export interface PlaceReview {
  id?: string;
  userId: string;
  placeId: string;
  comment: string;
  isRecommended: boolean;
  createdAt?: string;
  updatedAt?: string;
  userName?: string | null;
  userAvatar?: string | null;
}
