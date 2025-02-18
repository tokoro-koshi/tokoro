'use client';

import Image from 'next/image';
import { useState } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import Star from '@/public/icons/star.svg';
import { Place } from '@/lib/types/place';
import styles from './interactive-showcase.module.css';
import { CarouselItem } from '@/components/ui/carousel';

interface PlaceCardProps {
  place: Place;
}

export default function PlaceCard({ place }: PlaceCardProps) {
  const [imageError, setImageError] = useState(false);

  return !imageError ? (
    <CarouselItem className={styles.carouselItem}>
      <Card>
        <CardContent className={styles.cardContent}>
          <div className={styles.ratingContainer}>
            <div className={styles.ratingBackground}></div>
            <Image
              src={Star}
              alt={'Rating'}
              width={12}
              height={11}
              className={styles.star}
            />
            <p className={styles.rating}>{place.rating}</p>
          </div>
          <Image
            src={place.pictures[0]}
            alt={place.name}
            width={350}
            height={192}
            className={styles.image}
            onError={() => setImageError(true)}
          />
          <div className={styles.text}>
            <h3 className={styles.placeName}>{place.name}</h3>
            <div className={styles.placeDescription}>
              <p>{place.categoryId}</p>
              <p>{place.location.address}</p>
              <p>{place.description}</p>
            </div>
          </div>
        </CardContent>
      </Card>
    </CarouselItem>
  ) : null;
}