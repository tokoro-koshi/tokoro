'use client';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarHeader, SidebarMenuItem,
} from '@/components/ui/sidebar';
import { useRouter } from 'next/navigation';

export function AppSidebar() {
  const router = useRouter();
  
  return (
    <Sidebar className="overflow-y-auto !h-[calc(100vh-59px)] sticky top-[59px] bottom-0">
      <SidebarHeader>
        Create Chat
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup />
        <SidebarMenuItem onClick={()=>{router.push('/prompt/4')}}>
          Chat 1
        </SidebarMenuItem>
        <SidebarGroup />
      </SidebarContent>
    </Sidebar>
  )
}