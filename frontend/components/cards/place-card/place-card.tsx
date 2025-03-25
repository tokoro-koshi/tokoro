import Image from 'next/image';
import Link from 'next/link';
import { Place } from '@/lib/types/place';
import routes from '@/lib/constants/routes';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import Rating from '@/components/cards/items/rating/rating';
import SaveButton from '@/components/cards/items/save/save';
import styles from './place-card.module.css';

interface PlaceCardProps {
  place: Place;
}

export default function PlaceCard({ place }: PlaceCardProps) {
  const pictures = place.pictures.filter(Boolean);
  if (!pictures.length) pictures.push(routes.placeholder);

  return (
    <Link href={routes.place + '/' + place.id} target={'_blank'}>
      <Card>
        <CardHeader className={styles.cardHeader}>
            {place.rating !== 0 && (
              <Rating rating={place.rating} className='bottom-2' />
            )}
            <Image
              src={pictures[0]}
              alt={place.name}
              width={350}
              height={192}
              className={styles.image}
              quality={50}
            />
        </CardHeader>
        <CardContent className={styles.cardContent}>
          <div className={styles.text}>
            <h3 className={styles.name}>{place.name}</h3>
            <SaveButton
              className={styles.saveButton}
              placeId={place.id}
              variant={'dark'}
            />
            <p className={styles.category}>
              {place.categoryId.replace(/_+/, ' ')}
            </p>
            <p className={styles.location}>{place.location.address}</p>
            <p className={styles.description}>{place.description}</p>
          </div>
        </CardContent>
      </Card>
    </Link>
  );
}
