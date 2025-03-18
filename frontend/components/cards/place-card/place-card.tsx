import Image from 'next/image';
import { Place } from '@/lib/types/place';
import { Card, CardContent } from '@/components/ui/card';
import styles from './place-card.module.css';
import routes from '@/lib/constants/routes';
import Link from 'next/link';
import Rating from '@/components/cards/items/rating/rating';
import SaveButton from '@/components/cards/items/save/save';

interface PlaceCardProps {
  place: Place;
}

export default function PlaceCard({ place }: PlaceCardProps) {
  const pictures = place.pictures.filter(Boolean);
  if (!pictures.length) pictures.push(routes.placeholder);

  return (
    <Link href={routes.place + '/' + place.id} target={'_blank'}>
      <Card>
        <CardContent className={styles.cardContent}>
          <div className="relative">
            {place.rating !== 0 && <Rating rating={place.rating} className="bottom-2" />}
          <Image
            src={pictures[0]}
            alt={place.name}
            width={350}
            height={192}
            className={styles.image}
            quality={50}
          />
          </div>
          <div className={styles.text}>
            <h3 className={styles.name}>{place.name}</h3>
            <SaveButton className={styles.saveButton} placeId={""} variant={"dark"} />
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
