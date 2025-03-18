import { NextRequest, NextResponse } from 'next/server';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import { defaultCollectionName } from '@/lib/types/place';

/**
 * Get all collections for the current user.
 */
export async function POST(request: NextRequest) {
  const params = request.nextUrl.searchParams;
  const page = Number(params.get('page') ?? '0'),
    size = Number(params.get('size') ?? '20');

  const { payload: collections } = await FavoritesClient.getAllCurrentUserCollections(page, size);
  
  const { placeId } = await request.json();
  
  if (collections.filter(collection => collection.name === defaultCollectionName).length === 0) {
    const placesIds = [placeId];
    const savedCollection = await FavoritesClient.saveCollection({
      name: defaultCollectionName,
      placeIds: placesIds
    });
    console.log(savedCollection);
    return;
  } 
  
  const defaultCollection = collections.filter(collection => collection.name === defaultCollectionName)[0];
  
  if (defaultCollection.placesIds && defaultCollection.placesIds.includes(placeId)) {
    const s = await FavoritesClient.removeFavoritePlace(defaultCollection.id, placeId);
    console.log(s);
  }
  else {
    const s = await FavoritesClient.addFavoritePlace(defaultCollection.id, placeId);
    console.log(s);
  }

  return new NextResponse(null, { status: 204 });
}
