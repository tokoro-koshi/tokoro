﻿'use client';

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarHeader,
  SidebarMenuItem,
  SidebarTrigger,
} from '@/components/ui/sidebar';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { usePathname, useRouter } from 'next/navigation';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Button } from '@/components/ui/button';
import { SquarePen, Ellipsis, Trash } from 'lucide-react';
import { usePromptStore } from '@/lib/stores/prompt';
import { useSidebarStore } from '@/lib/stores/sidebar';
import { cn } from '@/lib/utils';
import styles from './sidebar.module.css';
import routes from '@/lib/constants/routes';
import React, { useEffect } from 'react';
import axios from 'axios';
import { useMutation } from '@tanstack/react-query';

export function AppSidebar() {
  const router = useRouter();
  const pathname = usePathname();
  const chats = usePromptStore((state) => state.chats);
  const deleteChat = usePromptStore((state) => state.deleteChat);
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

  const { mutate: deleteChatMutation } = useMutation({
    mutationFn: async (chatId: string | null) => {
      if (!chatId) return null;
      await axios.delete(`/api/chat-history/${chatId}`);
      return chatId;
    },
    onSuccess: (deletedChatId) => {
      // Check if the chat ID is valid
      if (!deletedChatId) return;

      // Remove the chat from the store
      deleteChat(deletedChatId);

      // Redirect to the search page if the deleted chat is the current one
      if (!pathname.startsWith('/prompt/')) return;

      // Get the current chat ID from the URL
      const currentChatId = pathname.split('/').pop();
      // Check if the current chat ID matches the deleted chat ID
      // If it does, redirect to the new prompt page
      if (currentChatId === deletedChatId) {
        router.push(routes.aiSearch);
      }
    },
  });

  const handleDeleteChat = (chatId: string, event: React.MouseEvent) => {
    event.stopPropagation();
    deleteChatMutation(chatId);
  };

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
          'max-md:bottom-0 max-md:z-40',
          'max-md:transition-all max-md:duration-300 max-md:ease-in-out'
        )}
        data-state={state}
      >
        <SidebarHeader className={styles.header}>
          <div className={styles.action}>
            <div
              className={cn(styles.toggle, state === 'expanded' && 'w-full')}
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
              onClick={() => {
                if (pathname == routes.aiSearch) {
                  window.location.reload();
                } else {
                  router.push(routes.aiSearch);
                }
              }}
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
                {chats.length === 0 ? (
                  <div className='flex justify-center py-52 text-center text-3xl font-bold text-sidebar-foreground'>
                    No chats yet...
                  </div>
                ) : (
                  chats.map((chat, index) => (
                    <SidebarMenuItem
                      key={index}
                      onClick={() => {
                        router.push(`/prompt/${chat.id}`);
                        if (
                          typeof window !== 'undefined' &&
                          window.innerWidth < 768
                        ) {
                          toggleSidebar();
                        }
                      }}
                      className={styles.menuItem}
                    >
                      <p className={'w-4/5 truncate'}>{chat.title}</p>

                      {chat.id && (
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button
                              variant='ghost'
                              size='sm'
                              className={styles.ellipsis}
                              onClick={(e) => e.stopPropagation()}
                            >
                              <Ellipsis className='h-5 w-5 stroke-[3]' />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align='end'>
                            <DropdownMenuItem
                              onClick={(event) =>
                                handleDeleteChat(chat.id, event)
                              }
                              className='bg-background font-medium text-sidebar hover:bg-white'
                            >
                              <Trash className='mr-2 h-5 w-5' />
                              Delete
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      )}
                    </SidebarMenuItem>
                  ))
                )}
              </SidebarGroup>
            </ScrollArea>
          </SidebarContent>
        )}
      </Sidebar>
    </>
  );
}
