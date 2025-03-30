'use client';

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarHeader,
  SidebarMenuItem,
} from '@/components/ui/sidebar';
import { useRouter } from 'next/navigation';
import styles from './sidebar.module.css';
import { chats } from '@/lib/constants/prompt/chats';
import { ScrollArea } from '@/components/ui/scroll-area';
import { SquarePen } from 'lucide-react';

export function AppSidebar() {
  const router = useRouter();

  return (
    <Sidebar className={styles.sidebar}>
      <SidebarHeader
        onClick={() => {
          router.push('/prompt');
        }}
        className={styles.header}
      >
        <div className={styles.newChat}>
          <span>New Chat</span>
          <SquarePen />
        </div>
      </SidebarHeader>
      <SidebarContent className={styles.content}>
        <ScrollArea className={styles.scrollArea}>
          <SidebarGroup className={styles.group}>
            {chats.map((chat, index) => (
              <SidebarMenuItem
                key={index}
                onClick={() => {
                  router.push(`/prompt/${chat.id}`);
                }}
                className={styles.menuItem}
              >
                {chat.title}
              </SidebarMenuItem>
            ))}
          </SidebarGroup>
        </ScrollArea>
      </SidebarContent>
    </Sidebar>
  );
}
