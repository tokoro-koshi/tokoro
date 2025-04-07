import { ChatHistoryClient } from '@/lib/requests/chat-history.client';
import Header from '@/components/layout/header/header';
import ChatWindow from '@/components/prompt/chat/chat-window';

type PromptPage = {
  params: {
    id: string;
  };
};

export default async function Prompt({ params }: PromptPage) {
  const [chats, chat] = await Promise.all([
    ChatHistoryClient.getAuthenticatedUserChatHistories(0, 100),
    ChatHistoryClient.getChatHistoryById(params.id),
  ]);
  return (
    <>
      <Header />
      <ChatWindow chats={chats} activeChat={chat}>
        {''}
      </ChatWindow>
    </>
  );
}
