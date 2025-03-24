import { useRouter } from 'next/navigation';
import { UserProfile } from '@auth0/nextjs-auth0/client';
import { User } from '@/lib/types/user';
import { ChevronRight, CreditCard, LogOut, Settings } from 'lucide-react';
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

export default function UserButton({ user }: { user: User | UserProfile }) {
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
          <DropdownMenuItem onClick={() => router.push(routes.settings)}>
            <Settings /> Settings
          </DropdownMenuItem>
          <DropdownMenuItem
            onClick={() => router.push(routes.support)}
            className='flex justify-between'
          >
            <CreditCard />
            <span>Support</span>
            <ChevronRight className='ml-auto' />
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
