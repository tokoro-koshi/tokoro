import apiClient from '@/lib/helpers/apiClient';
import { ChatHistory } from '@/lib/types/chat-history';

export class ChatHistoryClient {
    static async getChatHistoryById(id: string): Promise<ChatHistory> {
        const response = await apiClient.get<ChatHistory>(`/chat-histories/${id}`);
        return response.data;
    }

    static async deleteChatHistory(id: string): Promise<void> {
        await apiClient.delete(`/chat-histories/${id}`);
    }

    static async getAllChatsByUser(userId: string, page = 0, size = 20): Promise<ChatHistory[]> {
        const response = await apiClient.get<{ payload: ChatHistory[] }>(
            `/chat-histories/users/${userId}`,
            {
                params: { page, size },
            }
        );
        return response.data.payload;
    }
}
