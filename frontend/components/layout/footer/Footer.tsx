import Link from 'next/link'
import { Facebook, Twitter, Instagram } from 'lucide-react'
import styles from './Footer.module.css'

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.content}>
        <div className={styles.links}>
          <Link href="/privacy" className={styles.link}>Privacy Policy</Link>
          <Link href="/terms" className={styles.link}>Terms of Service</Link>
          <Link href="/contact" className={styles.link}>Contact Us</Link>
        </div>
        <div className={styles.social}>
          <a href="https://facebook.com" aria-label="Facebook" className={styles.socialLink}>
            <Facebook />
          </a>
          <a href="https://twitter.com" aria-label="Twitter" className={styles.socialLink}>
            <Twitter />
          </a>
          <a href="https://instagram.com" aria-label="Instagram" className={styles.socialLink}>
            <Instagram />
          </a>
        </div>
      </div>
      <p className={styles.copyright}>&copy; {new Date().getFullYear()} Tokoro. All rights reserved.</p>
    </footer>
  )
}

