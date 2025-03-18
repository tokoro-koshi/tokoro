import routes from '@/lib/constants/routes';
import { Button } from '@/components/buttons';
import styles from './header.module.css';

export default function Logination() {
  return (
    <>
      <a href={routes.auth.login} className={styles.navLink}>
        Login
      </a>
      <Button className={styles.roundedBtn} action={routes.auth.register}>
        Sign up
      </Button>
    </>
  );
}
