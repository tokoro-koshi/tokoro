import { Suspense } from 'react';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import { UserClient } from '@/lib/requests/user.client';
import CollectionsTable from '@/components/admin/collections/collections-table';
import CollectionsTableSkeleton from '@/components/admin/collections/collections-table-skeleton';
import styles from '@/components/admin/admin.module.css';

// Server-side data fetching using API client
async function fetchCollections() {
  try {
    // Get all collections for the current user
    const collectionsResponse =
      await FavoritesClient.getAllCurrentUserCollections();
    const collections = collectionsResponse.payload;

    // Enhance collections with usernames and additional details
    return await Promise.all(
      collections.map(async (collection) => {
        const user = await UserClient.getAuthenticatedUser();
        return {
          ...collection,
          userName: user?.name || 'Unknown User',
          placesCount: collection.placesIds.length,
          isPublic: false, // Assuming collections are private by default
        };
      })
    );
  } catch (error) {
    console.error('Error fetching collections:', error);
    return [];
  }
}

export default async function CollectionsPage() {
  const collections = await fetchCollections();
  return (
    <>
      <h1 className={styles.pageTitle}>Collections Management</h1>
      <Suspense fallback={<CollectionsTableSkeleton />}>
        <CollectionsTable initialCollections={collections} />
      </Suspense>
    </>
  );
}
