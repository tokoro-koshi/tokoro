import { ReactNode } from 'react';
import Link from "next/link";
import { Button as ShadcnButton } from '@/components/ui/button';

type ButtonArgs = {
    children: ReactNode,
    action: string | (() => void),
    className?: string,
}

export function Button({ children, action, className }: ButtonArgs) {
    return (typeof action === 'string'
        ? <Link href={action} className={className}>{children}</Link>
        : <ShadcnButton onClick={action} className={className}>{children}</ShadcnButton>);
}
