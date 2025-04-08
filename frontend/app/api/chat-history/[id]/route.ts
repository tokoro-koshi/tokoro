import { NextRequest, NextResponse } from 'next/server';
import { ChatHistoryClient } from '@/lib/requests/chat-history.client';

export async function DELETE(
  _request: NextRequest,
  { params }: { params: { id: string } }
) {
  await ChatHistoryClient.deleteChatHistory(params.id);
  return new NextResponse(null, { status: 204 });
}
