import { BackChat, Chat, FrontChatMessage } from '@/lib/types/prompt';
import apiClient from '@/lib/helpers/apiClient';
import { Pagination } from '@/lib/types/pagination';
import { PlaceClient } from '@/lib/requests/place.client';

export class ChatHistoryClient {
  static async getChatHistoryById(id: string): Promise<Chat | null> {
    try {
      const { data } = await apiClient.get<BackChat>(`/chat-histories/${id}`);
      const frontendMessages: FrontChatMessage[] = await Promise.all(
        data.messages.map(async (message): Promise<FrontChatMessage> => {
          if (message.sender === 'USER') {
            return message; // Directly return user messages
          }
          try {
            const places = await PlaceClient.getPlacesByIdArray(
              message.content
            );
            return { sender: 'AI', content: places };
          } catch (error) {
            console.error(error);
            return { sender: 'AI', content: [] };
          }
        })
      );
      return {
        ...data,
        messages: frontendMessages,
      };
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async deleteChatHistory(id: string): Promise<void> {
    await apiClient.delete(`/chat-histories/${id}`);
  }

  static async getUserChatHistories(
    userId: string,
    page: number = 0,
    size: number = 20
  ): Promise<BackChat[] | null> {
    try {
      const response = await apiClient.get<Pagination<BackChat>>(
        `/chat-histories/users/${userId}`,
        { params: { page, size } }
      );
      return response.data.payload;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getAuthenticatedUserChatHistories(
    page: number = 0,
    size: number = 20
  ): Promise<BackChat[] | null> {
    try {
      const response = await apiClient.get<Pagination<BackChat>>(
        `/chat-histories/users/me`,
        { params: { page, size } }
      );
      return response.data.payload;
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}
