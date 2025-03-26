import { Suspense } from 'react';
import { UserClient } from '@/lib/requests/user.client';
import UsersTable from '@/components/admin/users/users-table';
import UsersTableSkeleton from '@/components/admin/users/users-table-skeleton';
import styles from '@/components/admin/admin.module.css';

// Disable caching because of fetching that uses cookies
export const dynamic = 'force-dynamic';

export default async function UsersPage() {
  const users = await UserClient.getUsers();
  return (
    <div>
      <h1 className={styles.pageTitle}>User Management</h1>
      <Suspense fallback={<UsersTableSkeleton />}>
        <UsersTable initialUsers={users.payload} />
      </Suspense>
    </div>
  );
}
