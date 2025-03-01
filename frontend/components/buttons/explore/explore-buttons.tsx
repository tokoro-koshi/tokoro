'use client';

import { Button } from '@/components/buttons';
import routes from '@/lib/constants/routes';
import styles from './explore-buttons.module.css';
import { useUser } from '@/lib/stores/user';

export function ExploreButton() {
  const user = useUser((state) => state.user);
  return (
    <Button
      className={styles.exploreBtn}
      action={user ? routes.explore : routes.auth.register}
    >
      {user ? 'Start exploring!' : 'Sign up now'}
    </Button>
  );
}
