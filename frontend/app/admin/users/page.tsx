import { Suspense } from 'react';
import { UserClient } from '@/lib/requests/user.client';
import type { User } from '@/lib/types/user';
import UsersTable from '@/components/admin/users/users-table';
import UsersTableSkeleton from '@/components/admin/users/users-table-skeleton';
import styles from '@/components/admin/admin.module.css';

// Server component that fetches data
async function UsersContent() {
  // Fetch users on the server using the API client
  const users = await fetchUsers();

  return <UsersTable initialUsers={users} />;
}

// Server-side data fetching using API client
async function fetchUsers(): Promise<User[]> {
  try {
    // This would be a call to get all users
    // For now, we'll just get the authenticated user
    const user = await UserClient.getAuthenticatedUser();
    return user ? [user] : [];
  } catch (error) {
    console.error('Error fetching users:', error);
    return [];
  }
}

export default function UsersPage() {
  return (
    <div>
      <h1 className={styles.pageTitle}>User Management</h1>
      <Suspense fallback={<UsersTableSkeleton />}>
        <UsersContent />
      </Suspense>
    </div>
  );
}
