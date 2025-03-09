export interface PlaceReview {
  id?: string
  userId: string
  placeId: string
  comment: string
  isRecommended: boolean
  createdAt?: string
  updatedAt?: string
  // Additional fields for display
  userName?: string
  userAvatar?: string
}