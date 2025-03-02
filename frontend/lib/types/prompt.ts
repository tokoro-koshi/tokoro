import { Place } from '@/lib/types/place';

export interface ChatMessage {
  id: string;
  type: 'user' | 'ai';
  content: string;
  cards?: Place[];
}
