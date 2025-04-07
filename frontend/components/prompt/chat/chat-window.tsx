'use client';

import { BackChat, Chat } from '@/lib/types/prompt';
import { AppSidebar } from '@/components/prompt/sidebar/sidebar';
import ChatInterface from '@/components/prompt/chat/chat';
import { CSSProperties, ReactNode, useEffect } from 'react';
import { usePromptStore } from '@/lib/stores/prompt';
import { DateTime } from 'luxon';
import { useSidebarStore } from '@/lib/stores/sidebar';
import { useIsMobile } from '@/hooks/use-mobile';

type ChatWindowProps = {
  chats: BackChat[] | null;
  children: ReactNode;
  // if on /prompt/{id}, then it is fetched chat, else null
  activeChat?: Chat | null;
};

const SIDEBAR_WIDTH = '16rem';
const SIDEBAR_WIDTH_ICON = '3rem';

export default function ChatWindow({
  chats,
  children,
  activeChat,
}: ChatWindowProps) {
  const isMobile = useIsMobile();
  const updateChats = usePromptStore((state) => state.updateChats);
  const isSidebarClosed = useSidebarStore((state) => !state.open);

  useEffect(() => {
    updateChats(
      (chats ?? []).toSorted((a, b) =>
        DateTime.fromISO(b.createdAt)
          .diff(DateTime.fromISO(a.createdAt))
          .toMillis()
      )
    );
  }, [chats, updateChats]);

  return (
    <div
      style={
        {
          '--sidebar-width': SIDEBAR_WIDTH,
          '--sidebar-width-icon': SIDEBAR_WIDTH_ICON,
        } as CSSProperties
      }
      className={
        'group/sidebar-wrapper flex min-h-svh w-full has-[[data-variant=inset]]:bg-sidebar'
      }
    >
      <AppSidebar />
      {(!isMobile || isSidebarClosed) && (
        <section className='flex-1'>
          <ChatInterface activeChat={activeChat}>{children}</ChatInterface>
        </section>
      )}
    </div>
  );
}
