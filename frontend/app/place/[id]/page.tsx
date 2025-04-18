import { PlaceClient } from '@/lib/requests/place.client';
import Image from 'next/image';
import Rating from '@/components/cards/items/rating/rating';
import PlaceCarousel from '@/components/cards/place-carousel/place-carousel';
import styles from '../place.module.css';
import { cn } from '@/lib/utils';
import { Comments } from '@/components/place/comments/comments';
import { PlaceReviewClient } from '@/lib/requests/place-review.client';
import SaveButton from '@/components/cards/items/save/save';
import GoogleMapComponent from '@/components/map/google-map';
import Link from 'next/link';
import { notFound } from 'next/navigation';
import { ExternalLink } from 'lucide-react';

type PlacePageProps = {
  params: {
    id: string;
  };
};

export default async function PlacePage({ params }: PlacePageProps) {
  const place = await PlaceClient.getPlaceById(params.id);
  if (!place || !place.location) notFound();

  const suggestedPlaces = await PlaceClient.getRandomPlaces(20);
  const googleMapsLink = `https://maps.google.com/maps?q=${place.name}&ll=${place.location.coordinate.latitude},${place.location.coordinate.longitude}`;

  const placeReviews = await PlaceReviewClient.getPlaceReviews(params.id);

  return (
    <div className={cn('container', styles.container)}>
      <div className={styles.header}>
        <Rating rating={place.rating} className={styles.rating} />
        <div className={styles.imageContainer}>
          <Image
            className={styles.image}
            src={place.pictures[0]}
            alt={place.name}
            width={1440}
            height={900}
          />
          <div className={styles.shadow}></div>
        </div>
        <div className={styles.placeName}>
          <h1 className={styles.title}>{place.name}</h1>
          <ul className={styles.buttons}>
            <li>
              <a target={'_blank'} href={googleMapsLink}>
                <ExternalLink className={styles.mapsLink} />
              </a>
            </li>
            <li>
              <SaveButton
                variant={'light'}
                placeId={params.id}
                className={styles.saveButton}
              />
            </li>
          </ul>
        </div>
        <div className={styles.info}>
          <span className='font-light capitalize'>
            {place.categoryId.replace(/_+/, ' ')}
          </span>
          <a
            target='_blank'
            href={googleMapsLink}
            className={styles.addressLink}
          >
            <ExternalLink />
            {place.location.country}, {place.location.city},{' '}
            {place.location.address}
          </a>
        </div>
      </div>
      <ul className={styles.tags}>
        {place.tags
          .filter((tag) => tag.lang === 'en')
          .map((tag, index) => (
            <li className={styles.tag} key={index}>
              #{tag.name.replace(/_+/, ' ')}
            </li>
          ))}
      </ul>
      <div className={styles.middle}>
        <p className={styles.description}>{place.description}</p>
        <Link href={`/map/${place.id}`} className={styles.map}>
          <GoogleMapComponent
            places={[place]}
            center={{
              lat: place.location.coordinate.latitude,
              lng: place.location.coordinate.longitude,
            }}
            zoom={16}
          />
        </Link>
      </div>
      <PlaceCarousel className={styles.carousel} places={suggestedPlaces} />
      <Comments placeId={params.id} initialComments={placeReviews} />
    </div>
  );
}
