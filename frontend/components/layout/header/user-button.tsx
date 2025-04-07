import { useRouter } from 'next/navigation';
import { User } from '@/lib/types/user';
import {
  // ChevronRight,
  // CreditCard,
  // Settings,
  ClipboardList,
  LogOut,
  MapPinned,
  Newspaper,
  Search,
  Telescope,
} from 'lucide-react';
import routes from '@/lib/constants/routes';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import UserAvatar from '@/components/user/avatar';

export default function UserButton({ user }: { user: User }) {
  const router = useRouter();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        <UserAvatar user={user} />
      </DropdownMenuTrigger>

      <DropdownMenuContent className='mr-2.5 w-56'>
        <DropdownMenuLabel>{user.name}</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuGroup>
          {user.roles?.includes('ADMIN') && (
            <DropdownMenuItem onClick={() => router.push(routes.admin)}>
              <ClipboardList /> Admin
            </DropdownMenuItem>
          )}
          {/*<DropdownMenuItem onClick={() => router.push(routes.settings)}>*/}
          {/*  <Settings /> Settings*/}
          {/*</DropdownMenuItem>*/}
          {/*<DropdownMenuItem*/}
          {/*  onClick={() => router.push(routes.support)}*/}
          {/*  className='flex justify-between'*/}
          {/*>*/}
          {/*  <CreditCard />*/}
          {/*  <span>Support</span>*/}
          {/*  <ChevronRight className='ml-auto' />*/}
          {/*</DropdownMenuItem>*/}
          <DropdownMenuItem
            className={'md:hidden'}
            onClick={() => router.push(routes.explore)}
          >
            <Telescope /> Explore
          </DropdownMenuItem>
          <DropdownMenuItem
            className={'md:hidden'}
            onClick={() => router.push(routes.blog)}
          >
            <Newspaper /> Blog
          </DropdownMenuItem>
          <DropdownMenuItem
            className={'md:hidden'}
            onClick={() => router.push(routes.aiSearch)}
          >
            <Search /> AI Search
          </DropdownMenuItem>
          <DropdownMenuItem
            className={'md:hidden'}
            onClick={() => router.push(routes.map)}
          >
            <MapPinned /> Map
          </DropdownMenuItem>
        </DropdownMenuGroup>
        <DropdownMenuSeparator />
        <DropdownMenuGroup>
          <DropdownMenuItem onClick={() => router.push(routes.auth.logout)}>
            <LogOut /> Logout
          </DropdownMenuItem>
        </DropdownMenuGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
