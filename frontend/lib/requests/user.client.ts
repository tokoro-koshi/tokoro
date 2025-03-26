import apiClient from '@/lib/helpers/apiClient';
import { User } from '@/lib/types/user';
import { Pagination } from '@/lib/types/pagination';

export class UserClient {
  static async unblockUser(id: string): Promise<void> {
    await apiClient.post(`/users/${id}/unblock`);
  }

  static async assignRolesToUser(id: string, roles: string[]): Promise<void> {
    await apiClient.post(`/users/${id}/roles`, { roles });
  }

  static async removeRolesFromUser(id: string, roles: string[]): Promise<void> {
    await apiClient.delete(`/users/${id}/roles`, { data: { roles } });
  }

  static async blockUser(id: string): Promise<void> {
    await apiClient.post(`/users/${id}/block`);
  }

  static async updateUserAvatar(id: string, avatarUrl: string): Promise<void> {
    await apiClient.post(`/users/${id}/avatar`, { avatarUrl });
  }

  static async updateUserNickname(id: string, nickname: string): Promise<void> {
    await apiClient.patch(`/users/${id}/nickname`, { nickname });
  }

  static async updateUserName(id: string, name: string): Promise<void> {
    await apiClient.patch(`/users/${id}/name`, { name });
  }

  static async updateUserMetadata(
    id: string,
    metadata: Record<string, object>
  ): Promise<void> {
    await apiClient.patch(`/users/${id}/metadata`, metadata);
  }

  static async getUserDetails(id: string): Promise<User | null> {
    try {
      const encodedId = encodeURIComponent(id);
      const response = await apiClient.get<User>(`/users/${encodedId}`);
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async deleteUser(id: string): Promise<void> {
    await apiClient.delete(`/users/${id}`);
  }

  static async getUserPermissions(id: string): Promise<string[]> {
    const response = await apiClient.get<string[]>(`/users/${id}/permissions`);
    return response.data;
  }

  static async isUserBlocked(id: string): Promise<boolean> {
    const response = await apiClient.get<{ blocked: boolean }>(
      `/users/${id}/is-blocked`
    );
    return response.data.blocked;
  }

  static async getAuthenticatedUser(): Promise<User | null> {
    try {
      const { data } = await apiClient.get<User>('/users/me');
      return data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getUsers(
    page: number = 0,
    size: number = 20
  ): Promise<Pagination<User>> {
    const response = await apiClient.get<Pagination<User>>('/users', {
      params: { page, size },
    });
    return response.data;
  }

  static async getAuthenticatedUserByToken(
    accessToken: string
  ): Promise<User | null> {
    try {
      const { data } = await apiClient.get<User>('/users/me', {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      return data;
    } catch (error) {
      console.error('Failed to fetch user data:', error);
      return null;
    }
  }

  static async createUser(
    userData: Partial<User>
  ): Promise<User> {
    const response = await apiClient.post<User>('/users', userData);
    return response.data;
  }

  static async updateUser(
    id: string,
    userData: Partial<User>
  ): Promise<User> {
    const response = await apiClient.patch<User>(`/users/${id}`, userData);
    return response.data;
  }
}
