import { NextRequest, NextResponse } from 'next/server';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import { defaultCollectionName } from '@/lib/types/place';
import { UserClient } from '@/lib/requests/user.client';

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
    const placesIds = [placeId];
    await FavoritesClient.saveCollection({
      name: defaultCollectionName,
      placesIds: placesIds,
    });
    return;
  }

  if (
    defaultCollection.placesIds &&
    defaultCollection.placesIds.includes(placeId)
  ) {
    await FavoritesClient.removeFavoritePlace(defaultCollection.id, placeId);
  } else {
    await FavoritesClient.addFavoritePlace(defaultCollection.id, placeId);
  }

  await UserClient.getAuthenticatedUser();

  return new NextResponse(null, { status: 204 });
}
