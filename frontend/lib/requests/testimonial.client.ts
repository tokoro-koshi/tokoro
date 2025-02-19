import apiClient from '@/lib/helpers/apiClient';
import { Testimonial } from '@/lib/types/testimonial';

export class TestimonialClient {
  static async getTestimonialById(id: string): Promise<Testimonial> {
    const response = await apiClient.get<Testimonial>(`/testimonials/${id}`);
    return response.data;
  }

  static async updateTestimonial(id: string, testimonial: Testimonial): Promise<Testimonial> {
    const response = await apiClient.put<Testimonial>(`/testimonials/${id}`, testimonial);
    return response.data;
  }

  static async deleteTestimonial(id: string): Promise<void> {
    await apiClient.delete(`/testimonials/${id}`);
  }

  static async getAllTestimonials(): Promise<Testimonial[]> {
    const response = await apiClient.get<Testimonial[]>('/testimonials');
    return response.data;
  }

  static async saveTestimonial(testimonial: Testimonial): Promise<Testimonial> {
    const response = await apiClient.post<Testimonial>('/testimonials', testimonial);
    return response.data;
  }

  static async changeTestimonialStatus(id: string, status: string): Promise<void> {
    await apiClient.patch(`/testimonials/${id}/status`, { status });
  }

  static async getUserTestimonials(userId: string): Promise<Testimonial[]> {
    const response = await apiClient.get<Testimonial[]>(`/testimonials/user/${userId}`);
    return response.data;
  }

  static async getRandomTestimonials(count: number): Promise<Testimonial[]> {
    const response = await apiClient.get<Testimonial[]>(`/testimonials/random/${count}`);
    return response.data;
  }

  static async getTestimonialsByStatus(status: string): Promise<Testimonial[]> {
    const response = await apiClient.get<Testimonial[]>('/testimonials/by-status', {
      params: { status },
    });
    return response.data;
  }
}
