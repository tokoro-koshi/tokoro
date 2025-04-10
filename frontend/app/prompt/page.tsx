import styles from './prompt.module.css';
import Header from '@/components/layout/header/header';
import ChatWindow from '@/components/prompt/chat/chat-window';
import { ChatHistoryClient } from '@/lib/requests/chat-history.client';

export const dynamic = 'force-dynamic'; // force revalidation because of auth

export default async function NewPrompt() {
  const chats = await ChatHistoryClient.getAuthenticatedUserChatHistories(
    0,
    100
  );
  return (
    <>
      <Header />
      <ChatWindow chats={chats}>
        <div className={styles.container}>
          <h1 className={styles.heading}>
            <span>Describe</span> where you want to go,
          </h1>
          <p className={styles.paragraph}>And our AI Search will help you!</p>
        </div>
      </ChatWindow>
    </>
  );
}
