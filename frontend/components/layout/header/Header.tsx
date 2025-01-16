import Link from 'next/link'
import { Button } from '@/components/ui/button'
import styles from './Header.module.css'

export default function Header() {
  return (
    <header className={styles.header}>
      <div className={styles.logo}>Tokoro</div>
      <nav className={styles.nav}>
        <Link href="/" className={styles.navLink}>Home</Link>
        <Link href="#features" className={styles.navLink}>Features</Link>
        <Link href="#about" className={styles.navLink}>About</Link>
        <Link href="/login" className={styles.navLink}>Login</Link>
      </nav>
      <Button variant="outline">Register</Button>
    </header>
  )
}

