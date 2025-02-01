import { ReactNode } from 'react';
import Link from 'next/link';
import { Button as ShadcnButton } from '@/components/ui/button';

type ButtonArgs = {
  children: ReactNode;
  action: string | (() => void);
  className?: string;
};

export function Button({ children, action, className }: ButtonArgs) {
  if (
    typeof action === 'string' &&
    (action.startsWith('http') ||
      action.startsWith('mailto') ||
      action.startsWith('/api'))
  ) {
    return (
      <a href={action} className={className}>
        {children}
      </a>
    );
  }

  return typeof action === 'string' ? (
    <Link href={action} className={className}>
      {children}
    </Link>
  ) : (
    <ShadcnButton onClick={action} className={className}>
      {children}
    </ShadcnButton>
  );
}
