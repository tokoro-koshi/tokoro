'use client';

import { ReactNode, useEffect, useState } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
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

  return children;
}

export default function Providers({ children }: { children: ReactNode }) {
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            staleTime: 10 * 60 * 1000, // 10 minutes
            gcTime: 60 * 1000, // 1 minute
          },
        },
      })
  );

  return (
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <UserStoreHandler>{children}</UserStoreHandler>
      </UserProvider>
    </QueryClientProvider>
  );
}
