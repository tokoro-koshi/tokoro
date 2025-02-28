import { AppSidebar } from '@/components/prompt/sidebar/sidebar';
import { SidebarProvider } from '@/components/ui/sidebar';
import Header from '@/components/layout/header/header';
import React from 'react';

export default async function Prompt({ children }: { children: React.ReactNode }) {

  return (
    <>
      <Header />
      <SidebarProvider className="pt-[59px]">
        <AppSidebar />
        <main className='flex-grow bg-primary'>
          {children}
        </main>
      </SidebarProvider>
    </>
  );
}