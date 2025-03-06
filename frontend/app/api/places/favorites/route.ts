import { NextResponse } from 'next/server';
import { FavoritesClient } from '@/lib/requests/favorites.client';

/**
 * Get all collections for the current user.
 */
export async function GET() {
  const data = await FavoritesClient.getAllCurrentUserCollections();
  return NextResponse.json(data);
}
