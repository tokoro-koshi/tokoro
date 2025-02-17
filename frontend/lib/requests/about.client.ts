import apiClient from '@/lib/helpers/apiClient';
import { About } from '@/lib/types/about';

export class AboutClient {
  static async getAbout(): Promise<About> {
    const response = await apiClient.get<About>(`/about`);
    return response.data;
  }

  static async createAbout(about: About): Promise<About> {
    const response = await apiClient.post<About>(`/about`, about);
    return response.data;
  }

  static async updateAbout(about: About): Promise<About> {
    const response = await apiClient.put<About>(`/about`, about);
    return response.data;
  }

  static async deleteAbout(): Promise<void> {
    await apiClient.delete(`/about`);
  }
}
