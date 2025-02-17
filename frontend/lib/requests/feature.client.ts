import apiClient from '@/lib/helpers/apiClient';
import { Feature } from '@/lib/types/feature';

export class FeatureClient {
  static async getFeatureById(id: string): Promise<Feature> {
    const response = await apiClient.get<Feature>(`/features/${id}`);
    return response.data;
  }

  static async updateFeature(id: string, feature: Feature): Promise<Feature> {
    const response = await apiClient.put<Feature>(`/features/${id}`, feature);
    return response.data;
  }

  static async deleteFeature(id: string): Promise<void> {
    await apiClient.delete(`/features/${id}`);
  }

  static async getAllFeatures(): Promise<Feature[]> {
    const response = await apiClient.get<Feature[]>('/features');
    return response.data;
  }

  static async createFeature(feature: Feature): Promise<Feature> {
    const response = await apiClient.post<Feature>('/features', feature);
    return response.data;
  }
}
