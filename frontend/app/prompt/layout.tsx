import { AppSidebar } from '@/components/prompt/sidebar/sidebar';
import { SidebarProvider } from '@/components/ui/sidebar';
import Header from '@/components/layout/header/header';
import React from 'react';
import ChatInterface from '@/components/prompt/chat/chat';

export default async function Prompt({ children }: { children: React.ReactNode }) {

  return (
    <>
      <Header />
      <SidebarProvider>
        <AppSidebar />
        <main className='flex-1'>
          <ChatInterface>
            {children}
          </ChatInterface>
        </main>
      </SidebarProvider>
    </>
  );
}