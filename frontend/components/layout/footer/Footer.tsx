import { Facebook, Twitter, Instagram } from 'lucide-react'
import styles from './Footer.module.css'
import {footerButtons} from "@/lib/constants/components/footer";
import { Button } from "@/components/buttons/button";

// return (
//     <ul>{footerButtons.map((btn, idx) => <li key={idx}>idx</li>)}</ul>
// )

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.content}>
        <ul className={styles.links}>
          {footerButtons.map((btn, idx) => (
              <li key={idx}>
                <Button action={btn.action} className={styles.link}>
                  {btn.title}
                </Button>
              </li>
          ))}
        </ul>
        <p className={styles.copyright}>&copy; {new Date().getFullYear()} Tokoro. All rights reserved.</p>
        <div className={styles.social}>
          <a href="https://facebook.com" aria-label="Facebook" className={styles.socialLink}>
            <Facebook/>
          </a>
          <a href="https://twitter.com" aria-label="Twitter" className={styles.socialLink}>
            <Twitter/>
          </a>
          <a href="https://instagram.com" aria-label="Instagram" className={styles.socialLink}>
            <Instagram/>
          </a>
        </div>
      </div>
    </footer>
  )
}

