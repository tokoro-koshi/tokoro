﻿import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';
export async function POST(request: NextRequest): Promise<NextResponse> {
  const body = await request.json();
  const chat = await PlaceClient.searchPlaces(body.prompt, body.chatId);
  return NextResponse.json(chat);
}
