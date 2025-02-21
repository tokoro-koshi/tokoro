'use client';

import Link from 'next/link';
import Image from 'next/image';
import routes from '@/lib/constants/routes';
import { useUser } from '@/lib/stores/user';
import AuthButton from '@/components/buttons/auth-buttons';
import Logo from '@/public/logo.svg';
import styles from './header.module.css';

const baseRoutes = [{path:routes.home, label:"Home"},{path:routes.home+"#features", label:"Features"},{path:routes.home+"#about", label:"About"}];
const authRoutes = [{path:routes.home, label:"Home"},{path:routes.explore, label:"Explore"},{path:routes.blog, label:"Blog"},{path:routes.aiSearch, label:"AI Search"}];

export default function Header() {
  const { user, isLoading } = useUser();
  const currentRoutes = user ? authRoutes : baseRoutes;
  return (
    <header className={styles.header}>
      <Link href={routes.home} className={styles.logo}>
        <Image src={Logo} alt={'Tokoro Logo'} className={styles.logoImage} />
        <h1 className={styles.name}>Tokoro</h1>
      </Link>
      <nav className={styles.nav}>
        {currentRoutes.map((route, index) => 
          (<Link key={index} href={route.path} className={styles.navLink}>
          {route.label}
        </Link>))}
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
