'use client';

import Link from 'next/link';
import Image from 'next/image';
import routes from '@/lib/constants/routes';
import { baseRoutes, authRoutes } from '@/lib/constants/components/header';
import { useUser } from '@/lib/stores/user';
import Logination from '@/components/layout/header/logination';
import UserButton from '@/components/layout/header/user-button';
import Logo from '@/public/logo.svg';
import styles from './header.module.css';

export default function Header() {
  const { user, isLoading } = useUser();
  const currentRoutes = user ? authRoutes : baseRoutes;

  return (
    <header className={styles.header}>
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
          {user ? <UserButton user={user} /> : <Logination />}
        </div>
      )}
    </header>
  );
}
