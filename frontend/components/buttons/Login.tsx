import Link from 'next/link';
import { useUser } from '@auth0/nextjs-auth0/client';
import routes from '@/lib/constants/routes';
import { cn } from '@/lib/utils';
import { buttonVariants } from '@/components/ui/button';
import styles from './login.module.css';

export default function LoginButton() {
  const { user, isLoading } = useUser();
  if (isLoading) return null;
  
  return user ? (
    <Link href={routes.auth.logout} className={styles.loginButton}>Logout</Link>
  ) : (
    <>
      <Link href={routes.auth.login} className={styles.loginButton}>Login</Link>
      <Link
        href={routes.auth.register}
        className={cn(
          styles.registerButton, 
          buttonVariants({ variant: "outline" })
        )}
      >
        Register
      </Link>
    </>
  )
}