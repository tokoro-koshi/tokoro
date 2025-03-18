import { PlaceClient } from '@/lib/requests/place.client';
import Image from 'next/image';
import Rating from '@/components/cards/items/rating/rating';
import PlaceCarousel from '@/components/cards/place-carousel/place-carousel';
import styles from '../place.module.css';
import { cn } from '@/lib/utils';
import { Comments } from '@/components/place/comments/comments';
import { PlaceReviewClient } from '@/lib/requests/place-review.client';
import SaveButton from '@/components/cards/items/save/save';
import { getBearerToken } from '@/lib/helpers/auth0';

type PlacePageProps = {
  params: {
    id: string;
  };
};

export default async function PlacePage({ params }: PlacePageProps) {
  const place = await PlaceClient.getPlaceById(params.id);
  const suggestedPlaces = await PlaceClient.getRandomPlaces(20);
  const googleMapsLink = `https://maps.google.com/maps?q=${place.name}&ll=${place.location.coordinate.latitude},${place.location.coordinate.longitude}`;
  const googleMapsIframe = `https://www.google.com/maps/embed/v1/place?key=${process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY}&q=${place.name}&center=${place.location.coordinate.latitude},${place.location.coordinate.longitude}`;
  
  const placeReviews = await PlaceReviewClient.getPlacePlaceReviews(params.id);
  
  console.log(await getBearerToken());

  //TODO: User Avatar display on comments section
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
        <SaveButton variant={"light"} placeId={params.id} className={styles.saveButton}/>
        <div className={styles.info}>
          <span className="font-light capitalize">{place.categoryId.replace(/_+/, ' ')}</span>
          <a target='_blank' href={googleMapsLink} className="font-light capitalize">
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
      <div className={styles.middle}>
        <p className={styles.description}>
          {place.description}
        </p>
        <iframe
          className={styles.map}
          loading="lazy"
          allowFullScreen
          referrerPolicy="no-referrer-when-downgrade"
          src={googleMapsIframe}>
        </iframe>
      </div>
      <PlaceCarousel className={styles.carousel} places={suggestedPlaces} />
      <Comments
        placeId={params.id}
        initialComments={placeReviews}
      />
    </div>
  );
}