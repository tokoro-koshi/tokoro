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

  const { payload: collections } =
    await FavoritesClient.getAllCurrentUserCollections(page, size);

  const { placeId } = await request.json();

  const defaultCollection = collections.find(
    (collection) => collection.name === defaultCollectionName
  );

  if (!defaultCollection) {
    const newCollection = await FavoritesClient.saveCollection({
      name: defaultCollectionName,
      placesIds: [placeId],
    });
    return NextResponse.json(newCollection, { status: 201 });
  }

  if (
    defaultCollection.placesIds &&
    defaultCollection.placesIds.includes(placeId)
  ) {
    await FavoritesClient.removeFavoritePlace(defaultCollection.id, placeId);
    const collection = await FavoritesClient.getAllCurrentUserCollections();
    return NextResponse.json(collection);
  } else {
    await FavoritesClient.addFavoritePlace(defaultCollection.id, placeId);
    const collection = await FavoritesClient.getAllCurrentUserCollections();
    return NextResponse.json(collection);
  }
}
