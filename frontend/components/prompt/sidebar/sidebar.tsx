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
import { ScrollArea } from '@/components/ui/scroll-area';
import { SquarePen } from 'lucide-react';
import { useUser } from '@/lib/stores/user';
import { ChatHistoryClient } from '@/lib/requests/chat-history.client';
import { useQuery } from '@tanstack/react-query';
import toast from "react-hot-toast";

export function AppSidebar() {
  const router = useRouter();
  const { user } = useUser();
    
    const { data: chats } = useQuery({
        queryKey: ['ai-chat', user?.userId],
        queryFn: async () => {
            'use server';
            
            if (!user?.userId) return null;
            try {
                return await ChatHistoryClient.getUserChatHistories(user?.userId);
            } catch (error) {
                console.error(error);
                if ('message' in error) {
                    toast.error('Error fetching chat history:', error.message);
                }

                return null;
            }
        }
    });

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
            {chats?.map((chat, index) => (
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
