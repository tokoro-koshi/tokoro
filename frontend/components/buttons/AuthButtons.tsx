'use client';

import { UserProfile } from '@auth0/nextjs-auth0/client';
import { Button } from '@/components/buttons/button';
import routes from '@/lib/constants/routes';
import styles from './auth-buttons.module.css';

export default function AuthButton({ user }: { user?: UserProfile }) {
  return (
    <Button
      className={styles.roundedBtn}
      action={user ? routes.auth.logout : routes.auth.register}
    >
      {user ? 'Logout' : 'Sign up'}
    </Button>
  );
}
