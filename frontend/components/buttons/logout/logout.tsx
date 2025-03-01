import Link from 'next/link';
import routes from '@/lib/constants/routes';
import styles from '@/components/buttons/logout/logout.module.css';

export function LogoutButton() {
  return (
    <Link href={routes.auth.logout} className={styles.logoutButton}>
      Logout
    </Link>
  );
}
