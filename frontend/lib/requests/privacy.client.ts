import apiClient from '@/lib/helpers/apiClient';
import { Privacy } from '@/lib/types/privacy';

export class PrivacyClient {
  static async getPrivacyInfo(): Promise<Privacy> {
    const response = await apiClient.get<Privacy>(`/privacy`);
    return response.data;
  }

  static async createPrivacyInfo(privacy: Privacy): Promise<Privacy> {
    const response = await apiClient.post<Privacy>(`/privacy`, privacy);
    return response.data;
  }

  static async updatePrivacyInfo(privacy: Privacy): Promise<Privacy> {
    const response = await apiClient.put<Privacy>(`/privacy`, privacy);
    return response.data;
  }

  static async deletePrivacyInfo(): Promise<void> {
    await apiClient.delete(`/privacy`);
  }
}
