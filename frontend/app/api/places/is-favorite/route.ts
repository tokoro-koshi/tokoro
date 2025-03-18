import { NextRequest, NextResponse } from 'next/server';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import { defaultCollectionName } from '@/lib/types/place';

export async function POST(request: NextRequest) {
  const params = request.nextUrl.searchParams;
  const page = Number(params.get('page') ?? '0'),
    size = Number(params.get('size') ?? '20');

  const { payload: collections } = await FavoritesClient.getAllCurrentUserCollections(page, size);

  const { placeId } = await request.json();
  
  const defaultSavedPlaceIds = collections.filter(collection => collection.name === defaultCollectionName)[0].placesIds;
  
  if (collections.length > 0 && defaultSavedPlaceIds.includes(placeId) ) {
    return NextResponse.json({ isFavorite: true });
  }
  else {
    return NextResponse.json({ isFavorite: false });
  }
}
