'use client';

import Link from 'next/link'
import Image from 'next/image'
import routes from '@/lib/constants/routes';
import styles from './Header.module.css'
import Logo from '@/public/logo_without_text.svg'
import AuthButtons from '@/components/buttons/AuthButtons'
import React from "react";

export default function Header() {
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
                <Link href={'/about'} className={styles.navLink}>
                    About
                </Link>
            </nav>
            <AuthButtons />

        </header>
    );
}