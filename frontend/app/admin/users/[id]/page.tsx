import { notFound } from 'next/navigation';
import { Suspense } from 'react';
import { UserClient } from '@/lib/requests/user.client';
import UserProfile from '@/components/admin/users/user-profile';
import UserProfileSkeleton from '@/components/admin/users/user-profile-skeleton';
import styles from '@/components/admin/admin.module.css';

interface UserPageProps {
  params: {
    id: string;
  };
}

export default async function UserPage({ params }: UserPageProps) {
  const user = await UserClient.getUserDetails(params.id);
  if (!user) {
    notFound();
  }

  return (
    <div>
      <h1 className={styles.pageTitle}>User Details</h1>
      <Suspense fallback={<UserProfileSkeleton />}>
        <UserProfile user={user} />
      </Suspense>
    </div>
  );
}
