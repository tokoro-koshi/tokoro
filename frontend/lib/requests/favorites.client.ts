import apiClient from '@/lib/helpers/apiClient';
import { Pagination } from '@/lib/types/pagination';
import {
  FavoritesCollection,
  MutateFavoritesCollection,
} from '@/lib/types/place';

/**
 * Client for interacting with the collections API.
 */
export class FavoritesClient {
  /**
   * Save a new collection for the user.
   */
  static async saveCollection(
    data: MutateFavoritesCollection
  ): Promise<FavoritesCollection> {
    const response = await apiClient.post<FavoritesCollection>(
      `/collections`,
      data
    );
    return response.data;
  }

  /**
   * Get all collections for a user with pagination.
   */
  static async getAllCollections(
    userId: string,
    page: number = 0,
    size: number = 20
  ): Promise<Pagination<FavoritesCollection>> {
    const response = await apiClient.get<Pagination<FavoritesCollection>>(
      `/collections/${userId}?page=${page}&size=${size}`
    );
    return response.data;
  }

  /**
   * Get all collections for the current user with pagination.
   */
  static async getAllCurrentUserCollections(
    page: number = 0,
    size: number = 20
  ): Promise<Pagination<FavoritesCollection>> {
    const response = await apiClient.get<Pagination<FavoritesCollection>>(
      `/collections/me?page=${page}&size=${size}`
    );
    return response.data;
  }

  /**
   * Get a specific collection by its ID.
   */
  static async getCollectionById(
    collectionId: string
  ): Promise<FavoritesCollection> {
    const response = await apiClient.get<FavoritesCollection>(
      `/collections/${collectionId}`
    );
    return response.data;
  }

  /**
   * Update an existing collection.
   */
  static async updateCollection(
    collectionId: string,
    data: MutateFavoritesCollection
  ): Promise<FavoritesCollection> {
    const response = await apiClient.put<FavoritesCollection>(
      `/collections/${collectionId}`,
      data
    );
    return response.data;
  }

  /**
   * Delete a collection by its ID.
   */
  static async deleteCollection(collectionId: string): Promise<void> {
    await apiClient.delete(`/collections/${collectionId}`);
  }

  /**
   * Add a favorite place to a collection.
   */
  static async addFavoritePlace(
    collectionId: string,
    placeId: string
  ): Promise<FavoritesCollection> {
    const response = await apiClient.post<FavoritesCollection>(
      `/collections/${collectionId}/places`,
      placeId
    );
    return response.data;
  }

  /**
   * Remove a favorite place from a collection.
   */
  static async removeFavoritePlace(
    collectionId: string,
    placeId: string
  ): Promise<void> {
    await apiClient.delete(`/collections/${collectionId}/places/${placeId}`);
  }

  /**
   * Clear all favorite places from a collection.
   */
  static async clearCollection(collectionId: string): Promise<void> {
    await apiClient.delete(`/collections/${collectionId}/places`);
  }

  /**
   * Search for collections by name.
   */
  static async searchCollectionsByName(
    name: string
  ): Promise<FavoritesCollection[]> {
    const response = await apiClient.get<FavoritesCollection[]>(
      `/collections/search?name=${encodeURIComponent(name)}`
    );
    return response.data;
  }
}
