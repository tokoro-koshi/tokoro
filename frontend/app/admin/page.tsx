import { Suspense } from 'react';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { UserClient } from '@/lib/requests/user.client';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import { PlaceClient } from '@/lib/requests/place.client';
import { UserProfile as User } from '@auth0/nextjs-auth0/client';
import UserStatsCard from '@/components/admin/dashboard/user-stats-card';
import CollectionsStatsCard from '@/components/admin/dashboard/collections-stats-card';
import RecentUsersTable from '@/components/admin/dashboard/recent-users-table';
import ActivityChart from '@/components/admin/dashboard/activity-chart';
import DashboardSkeleton from '@/components/admin/dashboard/dashboard-skeleton';
import styles from '@/components/admin/admin.module.css';

// Server-side data fetching functions using API clients
async function fetchRecentUsers(): Promise<User[]> {
  try {
    // Get the authenticated user
    const user = await UserClient.getAuthenticatedUser();
    if (!user) return [];

    // In a real implementation, you'd have a specific endpoint for recent users
    // For now, we'll just return the authenticated user
    return [user];
  } catch (error) {
    console.error('Error fetching recent users:', error);
    return [];
  }
}

interface UserStats {
  total: number;
  active: number;
  growth: number;
}

async function fetchUserStats(): Promise<UserStats> {
  try {
    // In a real implementation, you'd have an endpoint for stats
    // For now, we'll just return some placeholder data
    return {
      total: 1249,
      active: 879,
      growth: 12.5,
    };
  } catch (error) {
    console.error('Error fetching user stats:', error);
    return {
      total: 0,
      active: 0,
      growth: 0,
    };
  }
}

interface CollectionStats {
  total: number;
  popular: string;
  growth: number;
}

async function fetchCollectionsStats(): Promise<CollectionStats> {
  try {
    // Get collections for the current user
    const collectionsResponse =
      await FavoritesClient.getAllCurrentUserCollections();
    const collections = collectionsResponse.payload;

    return {
      total: collections.length,
      popular: collections.length > 0 ? collections[0].name : 'None',
      growth: 8.2,
    };
  } catch (error) {
    console.error('Error fetching collections stats:', error);
    return {
      total: 0,
      popular: 'None',
      growth: 0,
    };
  }
}

interface PlaceStats {
  total: number;
  active: number;
  growth: number;
}

async function fetchPlacesStats(): Promise<PlaceStats> {
  try {
    // Get all places
    const places = await PlaceClient.getAllPlaces();

    return {
      total: places.length,
      active: places.length,
      growth: 15.3,
    };
  } catch (error) {
    console.error('Error fetching places stats:', error);
    return {
      total: 0,
      active: 0,
      growth: 0,
    };
  }
}

export default async function AdminDashboard() {
  const recentUsers = await fetchRecentUsers();
  const userStats = await fetchUserStats();
  const collectionsStats = await fetchCollectionsStats();
  const placesStats = await fetchPlacesStats();

  return (
    <div>
      <h1 className={styles.pageTitle}>Dashboard</h1>
      <Suspense fallback={<DashboardSkeleton />}>
        <div className='space-y-6'>
          <div className={styles.cardGrid}>
            <UserStatsCard stats={userStats} />
            <CollectionsStatsCard stats={collectionsStats} />

            <Card>
              <CardHeader className={styles.statCard}>
                <CardTitle className='text-sm font-medium'>
                  Active Places
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className={styles.statValue}>{placesStats.active}</div>
                <p className={styles.statChange}>
                  {placesStats.growth > 0 ? '+' : ''}
                  {placesStats.growth}% from last month
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className={styles.statCard}>
                <CardTitle className='text-sm font-medium'>
                  Conversion Rate
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className={styles.statValue}>24.3%</div>
                <p className={styles.statChange}>+5.1% from last week</p>
              </CardContent>
            </Card>
          </div>

          <Tabs defaultValue='overview' className={styles.tabsContainer}>
            <TabsList>
              <TabsTrigger value='overview'>Overview</TabsTrigger>
              <TabsTrigger value='analytics'>Analytics</TabsTrigger>
              <TabsTrigger value='reports'>Reports</TabsTrigger>
            </TabsList>

            <TabsContent value='overview' className='space-y-4'>
              <div className={styles.chartContainer}>
                <Card className={styles.chartCard}>
                  <CardHeader>
                    <CardTitle>Activity Overview</CardTitle>
                  </CardHeader>
                  <CardContent className='pl-2'>
                    <ActivityChart />
                  </CardContent>
                </Card>

                <Card className={styles.usersCard}>
                  <CardHeader>
                    <CardTitle>Recent Users</CardTitle>
                    <CardDescription>
                      {recentUsers.length} users registered recently
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <RecentUsersTable users={recentUsers} />
                  </CardContent>
                </Card>
              </div>
            </TabsContent>

            <TabsContent value='analytics' className='space-y-4'>
              <Card>
                <CardHeader>
                  <CardTitle>Analytics</CardTitle>
                  <CardDescription>
                    Detailed analytics will be displayed here.
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <p>Analytics content will be implemented here.</p>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value='reports' className='space-y-4'>
              <Card>
                <CardHeader>
                  <CardTitle>Reports</CardTitle>
                  <CardDescription>Generate and view reports.</CardDescription>
                </CardHeader>
                <CardContent>
                  <p>Reports content will be implemented here.</p>
                </CardContent>
              </Card>
            </TabsContent>
          </Tabs>
        </div>
      </Suspense>
    </div>
  );
}
