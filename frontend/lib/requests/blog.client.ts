import apiClient from '@/lib/helpers/apiClient';
import { Blog } from '@/lib/types/blog';

export class BlogClient {
  static async getBlogById(id: string): Promise<Blog> {
    const response = await apiClient.get<Blog>(`/blogs/${id}`);
    return response.data;
  }

  static async updateBlog(id: string, blog: Blog): Promise<Blog> {
    const response = await apiClient.put<Blog>(`/blogs/${id}`, blog);
    return response.data;
  }

  static async deleteBlog(id: string): Promise<void> {
    await apiClient.delete(`/blogs/${id}`);
  }

  static async getAllBlogs(): Promise<Blog[]> {
    const response = await apiClient.get<Blog[]>(`/blogs`);
    return response.data;
  }

  static async createBlog(blog: Blog): Promise<Blog> {
    const response = await apiClient.post<Blog>(`/blogs`, blog);
    return response.data;
  }
}
