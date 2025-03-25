import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Users } from 'lucide-react';
import styles from '@/components/admin/admin.module.css';

interface UserStatsProps {
  stats: {
    total: number;
    active: number;
    growth: number;
  };
}

export default function UserStatsCard({ stats }: UserStatsProps) {
  return (
    <Card>
      <CardHeader className={styles.statCard}>
        <CardTitle className='text-sm font-medium'>Total Users</CardTitle>
        <Users className='h-4 w-4 text-muted-foreground' />
      </CardHeader>
      <CardContent>
        <div className={styles.statValue}>{stats.total.toLocaleString()}</div>
        <p className={styles.statChange}>
          {stats.growth > 0 ? '+' : ''}
          {stats.growth}% from last month
        </p>
      </CardContent>
    </Card>
  );
}
