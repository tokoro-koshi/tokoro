import React from 'react';
import styles from '@/components/cards/place-carousel/place-carousel.module.css';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import PlaceCard from '@/components/cards/place-card/place-card';
import { Place } from '@/lib/types/place';
import { cn } from '@/lib/utils';

interface PlaceCarouselProps {
  places: Place[];
  className?: string;
}

export default function PlaceCarousel({
  places,
  className,
}: PlaceCarouselProps) {
  return (
    <Carousel
      autoplay
      opts={{
        align: 'center',
        slidesToScroll: 1,
        inViewThreshold: 0.7,
        // loop: true,
      }}
      className={cn(styles.carousel, className)}
      snappedCardClassName={styles.isSnapped}
    >
      <CarouselContent>
        {places.map((place) => (
          <CarouselItem key={place.id} className={styles.carouselItem}>
            <PlaceCard place={place} />
          </CarouselItem>
        ))}
      </CarouselContent>
      <CarouselPrevious className={styles.prevButton} />
      <CarouselNext className={styles.nextButton} />
    </Carousel>
  );
}
