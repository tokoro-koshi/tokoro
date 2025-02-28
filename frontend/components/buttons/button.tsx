import { ReactNode } from 'react';
import Link from 'next/link';
import { Button as ShadcnButton } from '@/components/ui/button';

type ButtonProps = {
  children: ReactNode;
  action: string | (() => void);
  className?: string;
  disabled?: boolean | null;
};

export function Button({ children, action, className, disabled }: ButtonProps) {
  if (
    typeof action === 'string' &&
    (action.startsWith('http') ||
      action.startsWith('mailto') ||
      action.startsWith('/api'))
  ) {
    return (
      <a href={action} className={className} aria-disabled={disabled ?? false}>
        {children}
      </a>
    );
  }

  return typeof action === 'string' ? (
    <Link href={action} className={className} aria-disabled={disabled ?? false}>
      {children}
    </Link>
  ) : (
    <ShadcnButton
      onClick={action}
      className={className}
      disabled={disabled ?? false}
    >
      {children}
    </ShadcnButton>
  );
}
