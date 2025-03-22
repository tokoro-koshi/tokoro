import apiClient from '@/lib/helpers/apiClient';
import { User } from '@/lib/types/user';
import { getSession } from '@auth0/nextjs-auth0/edge';
import { updateSession } from '@auth0/nextjs-auth0';
import { snakeToCamel } from '@/lib/helpers/snakeToCamel';


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

  static async getUserDetails(id: string): Promise<User|null> {
    try {
      const encodedId = encodeURIComponent(id);
      const response = await apiClient.get<User>(`/users/${encodedId}`);
      return response.data;
    }
    catch (error) {
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
    const response = await apiClient.get<{ blocked: boolean }>(`/users/${id}/is-blocked`);
    return response.data.blocked;
  }

  static async getAuthenticatedUser(): Promise<User|null> {
    try {
      const session = await getSession();

      const data = snakeToCamel((await apiClient.get<User>('/users/me')).data);

      if (session) {
        session.user = data;
        await updateSession(session);
      }

      return data;
    } catch (error) {
      console.error(error);
      return null;
    } 
  }

  static async getAuthenticatedUserByToken(accessToken: string): Promise<User | null> {
    try {
      return snakeToCamel((await apiClient.get<User>('/users/me', {
        headers: { Authorization: `Bearer ${accessToken}` },
      })).data);
    } catch (error) {
      console.error('Failed to fetch user data:', error);
      return null;
    }
  }
}
