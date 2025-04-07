import Header from '@/components/layout/header/header';
import { ReactNode } from 'react';
import { ChatHistoryClient } from '@/lib/requests/chat-history.client';
import ChatWindow from '@/components/prompt/chat/chat-window';

export default async function Prompt({ children }: { children: ReactNode }) {
  const chats = await ChatHistoryClient.getAuthenticatedUserChatHistories(
    0,
    100
  );
  return (
    <>
      <Header />
      <ChatWindow chats={chats}>{children}</ChatWindow>
    </>
  );
}
