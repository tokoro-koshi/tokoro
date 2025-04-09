import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';
import { BackChat, Chat, FrontChatMessage } from '@/lib/types/prompt';

export async function POST(request: NextRequest): Promise<NextResponse> {
  const body = await request.json();
  const chat = body.chat as BackChat;

  const messages: FrontChatMessage[] = await Promise.all(
    chat.messages.map(async (message): Promise<FrontChatMessage> => {
      if (message.sender === 'USER') {
        return message; // Directly return user messages
      }
      try {
        const places = await PlaceClient.getPlacesByIdArray(message.content);
        return { sender: 'AI', content: places };
      } catch (error) {
        console.error(error);
        return { sender: 'AI', content: [] };
      }
    })
  );

  return NextResponse.json({ ...chat, messages } as Chat);
}
