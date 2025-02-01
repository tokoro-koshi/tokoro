'use client';

import { ReactNode } from 'react';
import { UserProvider, useUser } from '@auth0/nextjs-auth0/client';
import { useUser as useUserStore } from '@/lib/stores/user';

function UserStoreHandler({ children }: { children: ReactNode }) {
  const { user, isLoading } = useUser();
  const setUser = useUserStore((state) => state.setUser);
  if (!isLoading) {
    setUser(user ?? null);
  }

  return <>{children}</>;
}

export default function Providers({ children }: { children: ReactNode }) {
  return (
    <UserProvider>
      <UserStoreHandler>{children}</UserStoreHandler>
    </UserProvider>
  );
}
