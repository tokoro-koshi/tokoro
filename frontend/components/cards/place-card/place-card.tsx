import Image from 'next/image';
import { Place } from '@/lib/types/place';
import { Card, CardContent } from '@/components/ui/card';
import Star from '@/public/icons/star.svg';
import styles from './place-card.module.css';
import routes from '@/lib/constants/routes';
import Link from 'next/link';

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
          <div className={styles.rating}>
            <div className={styles.background}></div>
            <Image
              src={Star}
              alt={'Rating'}
              width={12}
              height={11}
              className={styles.star}
            />
            <p className={styles.value}>{place.rating}</p>
          </div>
          <Image
            src={pictures[0]}
            alt={place.name}
            width={350}
            height={192}
            className={styles.image}
            quality={50}
          />
          <div className={styles.text}>
            <h3 className={styles.name}>{place.name}</h3>
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
