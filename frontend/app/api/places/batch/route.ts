import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';

export async function GET(request: NextRequest) {
  const ids = request.nextUrl.searchParams.get('ids')?.split(/,\s?/);
  if (!ids) {
    return NextResponse.json({ error: 'No IDs provided' }, { status: 400 });
  }
  return NextResponse.json(await PlaceClient.getPlacesByIdArray(ids));
}
