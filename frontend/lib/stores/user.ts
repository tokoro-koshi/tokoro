import { create } from 'zustand';
import { combine } from 'zustand/middleware';
import { User } from '@/lib/types/user';

type UserStore = {
  user: User | null;
  isLoading: boolean;
  setUser: (user: User | null) => void;
};

export const useUser = create<UserStore>(
  combine(
    // Initial state
    {
      user: null as User | null,
      isLoading: true,
    },
    // Actions
    (set) => ({
      setUser: (user: User | null) => set({ user, isLoading: false }),
    })
  )
);
