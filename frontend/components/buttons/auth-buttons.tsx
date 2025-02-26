'use client';

import { UserProfile } from '@auth0/nextjs-auth0/client';
import { Button } from '@/components/buttons';
import routes from '@/lib/constants/routes';
import styles from './auth-buttons.module.css';

type AuthButtonProps = {
  user?: UserProfile | null;
};

export function AuthButton({ user }: AuthButtonProps) {
  return (
    <Button
      className={styles.roundedBtn}
      action={user ? routes.auth.logout : routes.auth.register}
    >
      {user ? 'Logout' : 'Sign up'}
    </Button>
  );
}
