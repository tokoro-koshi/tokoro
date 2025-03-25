'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  LayoutDashboard,
  Users,
  Heart,
  Map,
  Settings,
  BarChart3,
  Shield,
} from 'lucide-react';
import { cn } from '@/lib/utils';
import styles from '@/components/admin/admin.module.css';

const navItems = [
  { href: '/admin', label: 'Dashboard', icon: LayoutDashboard },
  { href: '/admin/users', label: 'Users', icon: Users },
  { href: '/admin/collections', label: 'Collections', icon: Heart },
  { href: '/admin/places', label: 'Places', icon: Map },
  { href: '/admin/analytics', label: 'Analytics', icon: BarChart3 },
  { href: '/admin/roles', label: 'Roles', icon: Shield },
  { href: '/admin/settings', label: 'Settings', icon: Settings },
];

export default function AdminSidebar() {
  const pathname = usePathname();

  return (
    <aside className={styles.sidebar}>
      <nav className={styles.nav}>
        {navItems.map((item) => {
          const isActive = pathname === item.href;
          const Icon = item.icon;

          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                styles.navLink,
                isActive ? styles.navLinkActive : styles.navLinkInactive
              )}
            >
              <Icon className='h-4 w-4' />
              {item.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
