import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Heart } from 'lucide-react';
import styles from '@/components/admin/admin.module.css';

interface CollectionsStatsProps {
  stats: {
    total: number;
    popular: string;
    growth: number;
  };
}

export default function CollectionsStatsCard({ stats }: CollectionsStatsProps) {
  return (
    <Card>
      <CardHeader className={styles.statCard}>
        <CardTitle className='text-sm font-medium'>Collections</CardTitle>
        <Heart className='h-4 w-4 text-muted-foreground' />
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
