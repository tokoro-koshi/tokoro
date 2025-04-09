import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';

export async function GET(request: NextRequest): Promise<NextResponse> {
  const latitude =
    request.nextUrl.searchParams.get('lat') ?? '49.841328512070056';
  const longitude =
    request.nextUrl.searchParams.get('lng') ?? '24.02792769795437';
  const chat = await PlaceClient.getNearbyPlaces(+latitude, +longitude, 2);
  return NextResponse.json(chat);
}
