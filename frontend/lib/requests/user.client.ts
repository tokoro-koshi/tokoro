import apiClient from '@/lib/helpers/apiClient';
import { User } from '@/lib/types/user';


export class UserClient {
  static async unblockUser(id: string): Promise<void> {
    await apiClient.post(`/users/${id}/unblock`);
  }

  static async getUserRoles(id: string): Promise<string[]> {
    const response = await apiClient.get<string[]>(`/users/${id}/roles`);
    return response.data;
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

  static async updateUserMetadata(id: string, metadata: Record<string, object>): Promise<void> {
    await apiClient.patch(`/users/${id}/metadata`, metadata);
  }

  static async getUserDetails(id: string): Promise<User|undefined> {
    try {
      console.log('id', id);
      const encodedId = encodeURIComponent(id);
      const response = await apiClient.get<User>(`/users/${encodedId}`);
      console.log('response', response.data);
      return response.data;
    }
    catch {
      // console.error(error);
      return undefined;
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
    const response = await apiClient.get<{ blocked: boolean }>(`/users/${id}/is-blocked`);
    return response.data.blocked;
  }

  static async getAuthenticatedUser(): Promise<User> {
    const response = await apiClient.get<User>('/users/me');
    return response.data;
  }
}
