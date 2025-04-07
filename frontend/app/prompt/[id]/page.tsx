// import {ChatHistoryClient} from "@/lib/requests/chat-history.client";
// import ChatWindow from "@/components/prompt/chat/chat-window";

type PromptPage = {
  params: {
    id: string;
  };
};

export default async function Prompt({ params }: PromptPage) {
  // const chat = await ChatHistoryClient.getChatHistoryById(params.id);
  return (
    // <ChatWindow chats={[chat!]} activeChat={chat}>
    <div>Loading chat{params.id}...</div>
    // </ChatWindow>
  );
}
