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
import { cn } from '@/lib/utils';
import { useSidebar } from '@/components/ui/sidebar';
import styles from './sidebar.module.css';
import routes from "@/lib/constants/routes";

export function AppSidebar() {
  const router = useRouter();
  const chats = usePromptStore((state) => state.chats);
  const { state } = useSidebar(); // Отримуємо стан сайдбару

  return (
    <Sidebar
      className={cn(styles.sidebar, state === 'collapsed' && '!w-[4rem]')}
      data-state={state}
    >
      <SidebarHeader className={styles.header}>
        <div className={styles.action}>
          <div className={styles.toggle}>
            <SidebarTrigger
                className={cn(styles.trigger, state === 'collapsed' && 'h-6 w-6')}
            />
            <span className={cn(state !== 'collapsed' ? 'block' : 'hidden', styles.hide)}>
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
  );
}
