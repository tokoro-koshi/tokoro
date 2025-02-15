'use client';

import Link from 'next/link';
import Image from 'next/image';
import routes from '@/lib/constants/routes';
import { useUser } from '@/lib/stores/user';
import AuthButton from '@/components/buttons/auth-buttons';
import Logo from '@/public/logo.svg';
import styles from './header.module.css';

export default function Header() {
  const { user, isLoading } = useUser();
  return (
    <header className={styles.header}>
      <Link href={routes.home} className={styles.logo}>
        <Image src={Logo} alt={'Tokoro Logo'} className={styles.logoImage} />
        <h1 className={styles.name}>Tokoro</h1>
      </Link>
      <nav className={styles.nav}>
        <Link href={routes.home} className={styles.navLink}>
          Home
        </Link>
        <Link href={'#features'} className={styles.navLink}>
          Features
        </Link>
        <Link href={routes.about} className={styles.navLink}>
          About
        </Link>
      </nav>
      {!isLoading && (
        <div className={styles.authButtons}>
          {!user && (
            <a href={routes.auth.login} className={styles.navLink}>
              Login
            </a>
          )}
          <AuthButton user={user} />
        </div>
      )}
    </header>
  );
}
