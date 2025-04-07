'use client';

import { BackChat, Chat } from '@/lib/types/prompt';
import { AppSidebar } from '@/components/prompt/sidebar/sidebar';
import ChatInterface from '@/components/prompt/chat/chat';
import { SidebarProvider } from '@/components/ui/sidebar';
import { ReactNode, useEffect } from 'react';
import { usePromptStore } from '@/lib/stores/prompt';

type ChatWindowProps = {
  chats: BackChat[] | null;
  children: ReactNode;
  // if on /prompt/{id}, then it is fetched chat, else null
  activeChat?: Chat | null;
};

export default function ChatWindow({
  chats,
  children,
  activeChat,
}: ChatWindowProps) {
  const updateChats = usePromptStore((state) => state.updateChats);
  useEffect(() => {
    updateChats(chats ?? []);
  }, [chats, updateChats]);

  return (
    <SidebarProvider>
      <AppSidebar />
      <main className='flex-1'>
        <ChatInterface activeChat={activeChat}>{children}</ChatInterface>
      </main>
    </SidebarProvider>
  );
}
