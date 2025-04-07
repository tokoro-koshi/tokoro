'use client';

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarHeader,
  SidebarMenuItem,
  SidebarTrigger,
} from '@/components/ui/sidebar';
import { useRouter } from 'next/navigation';
import { ScrollArea } from '@/components/ui/scroll-area';
import { SquarePen } from 'lucide-react';
import { usePromptStore } from '@/lib/stores/prompt';
import { useSidebarStore } from '@/lib/stores/sidebar';
import { cn } from '@/lib/utils';
import styles from './sidebar.module.css';
import routes from '@/lib/constants/routes';
import { useEffect } from 'react';

export function AppSidebar() {
  const router = useRouter();
  const chats = usePromptStore((state) => state.chats);
  const open = useSidebarStore((state) => state.open);
  const toggleSidebar = useSidebarStore((state) => state.toggleSidebar);
  const state = useSidebarStore((state) =>
    state.open ? 'expanded' : 'collapsed'
  );

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (window.innerWidth < 768 && open) {
        const sidebarEl = document.querySelector('[data-sidebar="sidebar"]');
        if (sidebarEl && !sidebarEl.contains(e.target as Node)) {
          toggleSidebar();
        }
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [open, toggleSidebar]);

  return (
    // Backdrop overlay for mobile
    <>
      {open && (
        <div
          className='fixed inset-0 z-30 bg-black/30 transition-opacity duration-300 md:hidden'
          aria-hidden='true'
        />
      )}

      <Sidebar
        className={cn(
          styles.sidebar,
          state === 'collapsed' && '!w-[4rem]',
          // Mobile specific styles
          'max-md:!absolute max-md:bottom-0 max-md:top-0 max-md:z-40',
          open ? 'max-md:left-0' : 'max-md:-left-[364px]',
          'max-md:transition-all max-md:duration-300 max-md:ease-in-out'
        )}
        data-state={state}
      >
        <SidebarHeader className={styles.header}>
          <div className={styles.action}>
            <div
              className={cn(
                styles.toggle,
                state === 'collapsed' ? 'max-md:fixed' : 'w-full'
              )}
            >
              <SidebarTrigger
                className={cn(
                  styles.trigger,
                  state === 'collapsed' && 'h-6 w-6'
                )}
              />
              <span
                className={cn(
                  state !== 'collapsed' ? 'block' : 'hidden',
                  styles.hide
                )}
              >
                Hide
              </span>
            </div>

            <button
              className={cn(
                styles.newChat,
                state === 'collapsed' && 'justify-center'
              )}
              onClick={() => router.push(routes.aiSearch)}
            >
              <SquarePen
                className={state !== 'collapsed' ? 'h-6 w-6' : 'h-6 w-6'}
              />
              <span className={state !== 'collapsed' ? 'block' : 'hidden'}>
                New Chat
              </span>
            </button>
          </div>
        </SidebarHeader>

        {state !== 'collapsed' && (
          <SidebarContent className={styles.content}>
            <ScrollArea className={styles.scrollArea}>
              <SidebarGroup className={styles.group}>
                {chats.map((chat, index) => (
                  <SidebarMenuItem
                    key={index}
                    onClick={() => router.push(`/prompt/${chat.id}`)}
                    className={styles.menuItem}
                  >
                    {chat.title}
                  </SidebarMenuItem>
                ))}
              </SidebarGroup>
            </ScrollArea>
          </SidebarContent>
        )}
      </Sidebar>
    </>
  );
}
