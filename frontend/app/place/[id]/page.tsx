import { PlaceClient } from '@/lib/requests/place.client';
import Image from 'next/image';
import Rating from '@/components/cards/items/rating/rating';
import PlaceCarousel from '@/components/cards/place-carousel/place-carousel';
import styles from '../place.module.css';
import { cn } from '@/lib/utils';

type PlacePageProps = {
  params: {
    id: string;
  };
};

export default async function PlacePage({ params }: PlacePageProps) {
  const place = await PlaceClient.getPlaceById(params.id);
  const suggestedPlaces = await PlaceClient.getRandomPlaces(20);
  const googleMapsUrl = `https://maps.google.com/maps?q=${place.name}&ll=${place.location.coordinate.latitude},${place.location.coordinate.longitude}`;

  //TODO: Comments section
  //TODO: Save Button Component
  
  return (
    <div className={cn("container", styles.container)}>
      <div className={styles.header}>
        <Rating rating={place.rating} className={styles.rating} />
        <h1 className={styles.title}>{place.name}</h1>
        <div className={styles.imageContainer}>
          <Image className={styles.image} src={place.pictures[0]} alt={place.name} width={1440} height={900} />
          <div className={styles.shadow}></div>
        </div>
        <div className={styles.info}>
          <span className="font-light capitalize">{place.categoryId.replace(/_+/, ' ')}</span>
          <a target='_blank' href={googleMapsUrl} className="font-light capitalize">
            <span className="md:inline-block hidden">{place.location.country}, {place.location.city},</span> {place.location.address}
          </a>
        </div>
      </div>
      <ul className={styles.tags}>
        {place.tags.filter((tag) => Boolean(tag.lang === "en")).map((tag, index) => (
          <li className={styles.tag} key={index}>
            #{tag.name.replace(/_+/, ' ')}
          </li>
        ))}
      </ul>
      <p className={styles.description}>
        {place.description}
      </p>
      <PlaceCarousel className={styles.carousel} places={suggestedPlaces} />
    </div>
  );
}