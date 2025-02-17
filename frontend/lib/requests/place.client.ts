import apiClient from '@/lib/helpers/apiClient';
import { Place } from '@/lib/types/place';

export class PlaceClient {
  static async getPlaceById(id: string): Promise<Place> {
    const response = await apiClient.get<Place>(`/places/${id}`);
    return response.data;
  }

  static async updatePlace(id: string, place: Place): Promise<Place> {
    const response = await apiClient.put<Place>(`/places/${id}`, place);
    return response.data;
  }

  static async deletePlace(id: string): Promise<void> {
    await apiClient.delete(`/places/${id}`);
  }

  static async searchPlaces(tags: string[]): Promise<Place[]> {
    const response = await apiClient.post<Place[]>(`/places/search`, { tags });
    return response.data;
  }

  static async getAllPlaces(): Promise<Place[]> {
    const response = await apiClient.get<Place[]>(`/places`);
    return response.data;
  }

  static async savePlace(place: Place): Promise<Place> {
    const response = await apiClient.post<Place>(`/places`, place);
    return response.data;
  }

  static async getRandomPlaces(count: number): Promise<Place[]> {
    const response = await apiClient.get<Place[]>(`/places/random/${count}`);
    return response.data;
  }
}