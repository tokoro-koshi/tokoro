import { create } from 'zustand';
import { BackChat } from '@/lib/types/prompt';

interface PromptState {
  chats: BackChat[];
  addChat: (prompt: string) => void;
  updateChats: (chats: BackChat[]) => void;
}

export const usePromptStore = create<PromptState>((set) => ({
  chats: [],
  addChat: (prompt: string) =>
    set((state) => ({
      chats: [...state.chats, { title: prompt } as BackChat],
    })),
  updateChats: (chats: BackChat[]) => set({ chats }),
  // deleteChat: (id: string) => set((state) => ({
  //     chats: chats.filter((chat) => chat.id !== id)
  // })),
}));
