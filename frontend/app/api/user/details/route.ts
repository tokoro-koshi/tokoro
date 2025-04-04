import { NextResponse } from 'next/server';
import { UserClient } from '@/lib/requests/user.client';

export async function GET() {
  const user = await UserClient.getAuthenticatedUser();
  return NextResponse.json(user, { status: 200 });
}