import { NextRequest, NextResponse } from 'next/server';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import { PlaceClient } from '@/lib/requests/place.client';

export const dynamic = 'force-dynamic';

/**
 * Get all collections for the current user.
 */
export async function GET(request: NextRequest) {
  const params = request.nextUrl.searchParams;
  const page = Number(params.get('page') ?? '0'),
    size = Number(params.get('size') ?? '20');

  const { payload: collections } =
    await FavoritesClient.getAllCurrentUserCollections(page, size);
  const placesIds = collections.reduce(
    (acc, collection) => acc.concat(collection.placesIds),
    [] as string[]
  );

  const places = await PlaceClient.getPlacesByIdArray(placesIds);
  return NextResponse.json(places);
}
