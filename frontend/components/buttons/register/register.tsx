import Link from 'next/link';
import routes from '@/lib/constants/routes';
import styles from '@/components/buttons/register/register.module.css';
import { cn } from '@/lib/utils';
import { buttonVariants } from '@/components/ui/button';

export default function RegisterButton() {
  return (
    <Link
      href={routes.auth.register}
      className={cn(
        styles.registerButton,
        buttonVariants({ variant: 'outline' })
      )}
    >
      Register
    </Link>
  );
}
