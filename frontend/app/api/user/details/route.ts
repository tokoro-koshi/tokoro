﻿import { NextResponse } from 'next/server';
import { UserClient } from '@/lib/requests/user.client';

export const dynamic = 'force-dynamic'; // force revalidation because of auth

export async function GET() {
  const user = await UserClient.getAuthenticatedUser();
  return NextResponse.json(user, { status: 200 });
}
