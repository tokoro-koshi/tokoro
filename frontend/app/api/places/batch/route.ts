import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';

export async function GET(request: NextRequest) {
  const ids = request.nextUrl.searchParams.get('ids')?.split(/,\s?/).filter(id => id.trim() !== '');
  if (!ids || ids.length === 0) {
    return NextResponse.json({ error: 'No valid IDs provided' }, { status: 400 });
  }
  return NextResponse.json(await PlaceClient.getPlacesByIdArray(ids));
}
