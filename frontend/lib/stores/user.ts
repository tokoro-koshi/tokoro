import { create } from 'zustand';
import { combine } from 'zustand/middleware';
import { UserProfile } from '@auth0/nextjs-auth0/client';

type UserStore = {
  user: UserProfile | null;
  isLoading: boolean;
  setUser: (user: UserProfile | null) => void;
};

export const useUser = create<UserStore>(
  combine(
    // Initial state
    {
      user: null as UserProfile | null,
      isLoading: true,
    },
    // Actions
    (set) => ({
      setUser: (user: UserProfile | null) => set({ user, isLoading: false }),
    })
  )
);
