import { create } from 'zustand';
import { persist } from 'zustand/middleware';

type SidebarState = {
  open: boolean;
  toggleSidebar: () => void;
  setOpen: (open: boolean) => void;
};

export const useSidebarStore = create<SidebarState>()(
  persist(
    (set) => ({
      open: false,
      toggleSidebar: () => set((state) => ({ open: !state.open })),
      setOpen: (open) => set({ open }),
    }),
    {
      name: 'sidebar-storage', // key in localStorage
    }
  )
);
