'use client';

import { ReactNode, useEffect } from 'react';
import { UserProvider, useUser } from '@auth0/nextjs-auth0/client';
import { useUser as useUserStore } from '@/lib/stores/user';

function UserStoreHandler({ children }: { children: ReactNode }) {
  const { user, isLoading } = useUser();
  const setUser = useUserStore((state) => state.setUser);

  useEffect(() => {
    if (!isLoading) {
      setUser(user ?? null);
    }
  }, [isLoading, user, setUser]);

  return <>{children}</>;
}

export default function Providers({ children }: { children: ReactNode }) {
  return (
    <UserProvider>
      <UserStoreHandler>{children}</UserStoreHandler>
    </UserProvider>
  );
}
