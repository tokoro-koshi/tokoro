'use client';

import Link from 'next/link'
import routes from '@/lib/constants/routes';
import LoginButton from '@/components/buttons/Login';
import styles from './Header.module.css'

export default function Header() {
  return (
    <header className={styles.header}>
      <div className={styles.logo}>Tokoro</div>
      <nav className={styles.nav}>
        <Link href={routes.home} className={styles.navLink}>Home</Link>
        <LoginButton />
      </nav>
    </header>
  )
}
