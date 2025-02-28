import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/generic/place-card/place-card';
import styles from './place-list.module.css';
import { cn } from '@/lib/utils';

type PlaceListArgs = {
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
}: PlaceListArgs) {
  if (places.length === 0) {
    return (
      <div className='text-center text-gray-500'>
        {noPlacesMessage ?? 'No places found'}
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
