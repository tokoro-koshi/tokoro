'use client';

import Link from 'next/link'
import Image from 'next/image'
import {useUser} from "@auth0/nextjs-auth0/client";
import routes from '@/lib/constants/routes';
import Logo from '@/public/logo_without_text.svg'
import AuthButton from '@/components/buttons/AuthButtons'
import styles from './Header.module.css'

export default function Header() {
    const { user, isLoading } = useUser();
    return (
        <header className={styles.header}>
            <div className={styles.logo}>
                <Image src={Logo} alt="Tokoro Logo" className={styles.logoImage} />
                <span>Tokoro</span>
            </div>
            <nav className={styles.nav}>
                <Link href={routes.home} className={styles.navLink}>
                    Home
                </Link>
                <Link href='#features' className={styles.navLink}>
                    Features
                </Link>
                <Link href={routes.about} className={styles.navLink}>
                    About
                </Link>
                {!user && !isLoading && (
                    <Link href={routes.auth.login} className={styles.navLink}>
                        Login
                    </Link>
                )}
            </nav>
            <AuthButton user={user} />
        </header>
    );
}