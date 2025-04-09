import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';

export async function GET(
  _request: NextRequest,
  { params }: { params: { ids: string } }
) {
  const ids = params.ids.split(/,\s?/);
  return NextResponse.json(await PlaceClient.getPlacesByIdArray(ids));
}
