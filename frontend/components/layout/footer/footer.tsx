import { siFacebook, siX, siInstagram } from 'simple-icons';
import { footerButtons } from '@/lib/constants/components/footer';
import { Button } from '@/components/buttons';
import styles from './footer.module.css';

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <ul className={styles.links}>
        {footerButtons.map((btn, idx) => (
          <li key={idx}>
            <Button action={btn.action} className={styles.link}>
              {btn.title}
            </Button>
          </li>
        ))}
      </ul>
      <p className={styles.copyright}>
        &copy; {new Date().getFullYear()} Tokoro. All rights reserved.
      </p>
      <ul className={styles.socials}>
        <li className={styles.socialLink}>
          <a
            href={'https://facebook.com'}
            target={'_blank'}
            aria-label={'Facebook'}
            className={styles.socialLink}
            dangerouslySetInnerHTML={{ __html: siFacebook.svg }}
          />
        </li>
        <li className={styles.socialLink}>
          <a
            href={'https://twitter.com'}
            target={'_blank'}
            aria-label={'Twitter'}
            className={styles.socialLink}
            dangerouslySetInnerHTML={{ __html: siX.svg }}
          />
        </li>
        <li className={styles.socialLink}>
          <a
            href={'https://instagram.com'}
            target={'_blank'}
            aria-label={'Instagram'}
            className={styles.socialLink}
            dangerouslySetInnerHTML={{ __html: siInstagram.svg }}
          />
        </li>
      </ul>
    </footer>
  );
}
