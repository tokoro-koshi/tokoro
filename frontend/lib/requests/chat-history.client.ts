import {BackChat} from "@/lib/types/prompt";
import apiClient from '@/lib/helpers/apiClient';
import { Pagination } from '@/lib/types/pagination';

export class ChatHistoryClient{
    
    static async getChatHistoryById(id: string): Promise<BackChat> {
        const response = await apiClient.get<BackChat>(`/chat-histories/${id}`);
        return response.data;
    }

    static async deleteChatHistory(id: string): Promise<void> {
        await apiClient.delete(`/chat-histories/${id}`);
    }

    static async getUserChatHistories(userId: string, page: number = 0, size: number = 20): Promise<BackChat[]> {
        const response = await apiClient.get<Pagination<BackChat>>(`/chat-histories/users/${userId}`, {
            params: { page, size },
        });
        return response.data.payload;
    }
}