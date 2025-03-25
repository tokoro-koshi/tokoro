'use client';

import Link from 'next/link';
import Image from 'next/image';
import { Bell } from 'lucide-react';
import { Button } from '@/components/ui/button';
import UserButton from '@/components/layout/header/user-button';
import Logo from '@/public/logo.svg';
import { useUser } from '@/lib/stores/user';
import styles from '@/components/admin/admin.module.css';

export default function AdminHeader() {
  const { user, isLoading } = useUser();

  if (isLoading || !user) return null;

  return (
    <header className={styles.header}>
      <div className={styles.logoContainer}>
        <Link href='/admin' className={styles.logoContainer}>
          <Image
            src={Logo || '/placeholder.svg'}
            alt='Logo'
            width={32}
            height={32}
          />
          <span className={styles.logoText}>Admin Dashboard</span>
        </Link>
      </div>

      <div className={styles.headerActions}>
        <Button variant='outline' size='icon' className='h-8 w-8 rounded-full'>
          <Bell className='h-4 w-4' />
          <span className='sr-only'>Notifications</span>
        </Button>
        <UserButton user={user} />
      </div>
    </header>
  );
}
