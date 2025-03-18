import { useRouter } from 'next/navigation';
import { UserProfile } from '@auth0/nextjs-auth0/client';
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
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

export default function UserButton({ user }: { user: UserProfile }) {
  const router = useRouter();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Avatar>
          <AvatarImage src={user.picture ?? ''} />
          <AvatarFallback className={'text-white'}>
            {user.name
              ?.split(' ')
              .map((x) => x.charAt(0))
              .join('')}
          </AvatarFallback>
        </Avatar>
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
