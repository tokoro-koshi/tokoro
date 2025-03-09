'use client';

import Link from 'next/link';
import Image from 'next/image';
import routes from '@/lib/constants/routes';
import { useUser } from '@/lib/stores/user';
import Logo from '@/public/logo.svg';
import styles from './header.module.css';
import { Button } from '@/components/buttons/button';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { ChevronRight, CreditCard, LogOut, Settings } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { baseRoutes, authRoutes } from '@/lib/constants/components/header';
import { cn } from '@/lib/utils';

interface HeaderProps {
  isTransparent?: boolean;
}

export default function Header({ isTransparent = false }: HeaderProps) {
  const router = useRouter();
  const { user, isLoading } = useUser();
  const currentRoutes = user ? authRoutes : baseRoutes;

  return (
    <header
      className={cn(styles.header, {
        'bg-opacity-65': isTransparent,
      })}
    >
      <Link href={routes.home} className={styles.logo}>
        <Image src={Logo} alt={'Logo'} className={styles.logoImage} />
        <h1 className={styles.name}>Tokoro</h1>
      </Link>
      <nav className={styles.nav}>
        {currentRoutes.map((route, index) => (
          <Link key={index} href={route.path} className={styles.navLink}>
            {route.label}
          </Link>
        ))}
      </nav>
      {!isLoading && (
        <div className={styles.authButtons}>
          {user ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Avatar className={styles.avatar}>
                  <AvatarImage src={user?.picture ?? ''} />
                  <AvatarFallback>{user?.name?.substring(0, 3)}</AvatarFallback>
                </Avatar>
              </DropdownMenuTrigger>

              <DropdownMenuContent className='mr-2.5 w-56'>
                <DropdownMenuLabel>{user?.name}</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuGroup>
                  <DropdownMenuItem
                    onClick={() => router.push(routes.settings)}
                  >
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
                  <DropdownMenuItem
                    onClick={() => (window.location.href = routes.auth.logout)}
                  >
                    <LogOut /> Logout
                  </DropdownMenuItem>
                </DropdownMenuGroup>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <>
              <a href={routes.auth.login} className={styles.navLink}>
                Login
              </a>
              <Button
                className={styles.roundedBtn}
                action={user ? routes.auth.logout : routes.auth.register}
              >
                Sign up
              </Button>
            </>
          )}
        </div>
      )}
    </header>
  );
}
