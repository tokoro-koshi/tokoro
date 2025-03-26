import apiClient from '@/lib/helpers/apiClient';
import { Place } from '@/lib/types/place';
import { Pagination } from '@/lib/types/pagination';

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

  static async searchPlaces(prompt: string): Promise<Place[]> {
    const response = await apiClient.post<Place[]>(`/places/search`, {
      prompt,
    });
    return response.data;
  }

  static async getAllPlaces(
    page: number = 0,
    size: number = 20
  ): Promise<Place[]> {
    const response = await apiClient.get<Pagination<Place>>(`/places`, {
      params: { page, size },
    });
    return response.data.payload;
  }

  static async getPlacesByIdArray(ids: string[]): Promise<Place[]> {
    const response = await apiClient.get<Place[]>(`/places/batch`, {
      params: { ids: ids.join(',') },
    });
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
