import { Place } from '@/lib/types/place';

export interface UserChatMessage {
  sender: 'USER';
  content: string[];
}

export interface AiBackChatMessage {
  sender: 'AI';
  content: string[];
}

export interface AiFrontChatMessage {
  sender: 'AI';
  content: Place[];
}

export type BackChatMessage = UserChatMessage | AiBackChatMessage;
export type FrontChatMessage = UserChatMessage | AiFrontChatMessage;

export interface BackChat {
  id: string;
  title: string;
  userId: string;
  messages: BackChatMessage[];
  createdAt: string;
}

export interface Chat {
  id: string;
  title: string;
  userId: string;
  messages: FrontChatMessage[];
  createdAt: string;
}
