export interface Message {
    sender: 'user' | 'ai'; // Enum: User or AI
    content: string;
}

export interface ChatHistory {
    id: string;
    title: string;
    userId: string;
    messages: Message[];
    createdAt: string; // ISO 8601 date-time format
}
