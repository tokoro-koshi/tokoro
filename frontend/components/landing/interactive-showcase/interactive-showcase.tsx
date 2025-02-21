import {
  Carousel,
  CarouselContent,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/landing/interactive-showcase/place-card';
import styles from './interactive-showcase.module.css';

interface InteractiveShowcaseProps {
  places: Place[];
}

export default function InteractiveShowcase({ places }: InteractiveShowcaseProps) {
  return (
    <section className={styles.showcase} id="features">
      <div className={styles.titleSection}>
        <div className={styles.line}></div>
        <h2 className={styles.title}>Explore Amazing Places</h2>
        <div className={styles.line}></div>
      </div>
      <Carousel
        autoplay
        opts={{
          align: 'center',
          slidesToScroll: 1,
          inViewThreshold: 0.7,
          // loop: true,
        }}
        className={styles.carousel}
        snappedCardClassName={styles.isSnapped}
      >
        <CarouselContent>
          {places.map((place: Place, index: number) => (
            <PlaceCard key={index} place={place} />
          ))}
        </CarouselContent>
        <CarouselPrevious className={styles.prevButton} />
        <CarouselNext className={styles.nextButton} />
      </Carousel>
    </section>
  );
}