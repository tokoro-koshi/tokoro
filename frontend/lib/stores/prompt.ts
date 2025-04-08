import { create } from 'zustand';
import { BackChat } from '@/lib/types/prompt';

interface PromptState {
  chats: BackChat[];
  addChat: (prompt: string, chatId?: string) => void;
  updateChats: (chats: BackChat[]) => void;
  deleteChat: (id: string) => void;
}

export const usePromptStore = create<PromptState>((set) => ({
  chats: [],
  addChat: (prompt: string, chatId?: string) =>
    set((state) => {
      if (chatId) {
        return {
          chats: state.chats.map(chat =>
              chat.id === chatId ? {...chat, title: prompt} : chat
          )
        };
      }
      return {
        chats: [{title: prompt} as BackChat, ...state.chats],
      };
    }),
  updateChats: (chats: BackChat[]) => set({ chats }),
  deleteChat: (id: string) => set((state) => ({
      chats: state.chats.filter((chat) => chat.id !== id)
  })),
}));
