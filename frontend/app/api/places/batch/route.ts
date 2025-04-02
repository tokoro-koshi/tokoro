import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';
import { BackChat, Chat, AiFrontChatMessage, FrontChatMessage } from "@/lib/types/prompt";

export async function POST(request: NextRequest): Promise<NextResponse> {
    const body = await request.json();
    const chat = body.chat as BackChat;

    const messages: FrontChatMessage[] = await Promise.all(
        chat.messages.map(async (message): Promise<FrontChatMessage> => {
            if (message.sender === 'USER') {
                return message; // Directly return user messages (string[])
            }

            // Fetch places asynchronously for AI messages
            const places = await PlaceClient.getPlacesByIds(message.content);
            return { sender: "AI", content: places } as AiFrontChatMessage;
        })
    );

    console.log(messages);
    return NextResponse.json({ ...chat, messages } as Chat);
}
