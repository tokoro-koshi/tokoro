import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';
// import { Chat } from "@/lib/types/prompt";

export async function POST(request: NextRequest): Promise<NextResponse> {
  const body = await request.json();
  const chat = await PlaceClient.searchPlaces(body.prompt, body.chatId);
  // const userChat = { ...chat } as Chat;
  //
  // for (let index = 0; index < chat.messages.length; index++) {
  //   const message = chat.messages[index];
  //   if (message.sender === "USER") continue;
  //   userChat.messages[index].content = await PlaceClient.getPlacesByIds(message.content);
  // }
  //
  // console.log(userChat);
  return NextResponse.json(chat);
}
