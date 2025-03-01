import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/generic/place-card/place-card';
import styles from './place-list.module.css';
import { cn } from '@/lib/utils';
import { Card, CardContent, CardHeader } from '@/components/ui/card';

type PlaceListProps = {
  places: Place[];
  columns?: {
    base?: number | 'auto';
    sm?: number | 'auto';
    md?: number | 'auto';
    lg?: number | 'auto';
    xl?: number | 'auto';
    '2xl'?: number | 'auto';
  } | null;
  noPlacesMessage?: string | null;
};

export default function PlaceList({
  places,
  noPlacesMessage,
  columns = { base: 1, lg: 3 },
}: PlaceListProps) {
  if (places.length === 0) {
    return (
      <div className={styles.notFound}>
        <Card className={styles.card}>
          <CardHeader className={styles.cardHeader}>🤷</CardHeader>
          <CardContent>
            {noPlacesMessage ?? 'Places not found'}
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <ul
      className={cn(styles.placesList, {
        [`grid-cols-${columns?.base}`]: columns?.base,
        [`sm:grid-cols-${columns?.sm}`]: columns?.sm,
        [`md:grid-cols-${columns?.md}`]: columns?.md,
        [`lg:grid-cols-${columns?.lg}`]: columns?.lg,
        [`xl:grid-cols-${columns?.xl}`]: columns?.xl,
        [`2xl:grid-cols-${columns?.['2xl']}`]: columns?.['2xl'],
      })}
    >
      {places.map((place) => (
        <li key={place.id} className={styles.place}>
          <PlaceCard place={place} />
        </li>
      ))}
    </ul>
  );
}
