import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';

export async function GET(request: NextRequest) {
  const countStr = request.nextUrl.searchParams.get('count') ?? '20';
  return NextResponse.json(await PlaceClient.getRandomPlaces(+countStr));
}
