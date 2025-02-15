import Image from 'next/image';
import { places } from '@/lib/constants/landing/interactive-showcase';
import { Card, CardContent } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import Star from '@/public/icons/star.svg';
import styles from './interactive-showcase.module.css';

export default function InteractiveShowcase() {
  return (
    <section className={styles.showcase}>
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
        }}
        className={styles.carousel}
        snappedCardClassName={styles.isSnapped}
      >
        <CarouselContent>
          {places.map((place, index) => (
            <CarouselItem key={index} className={styles.carouselItem}>
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
                    src={place.image || '/placeholder.svg'}
                    alt={place.name}
                    width={350}
                    height={195}
                    className={styles.image}
                  />
                  <div className={styles.text}>
                    <h3 className={styles.placeName}>{place.name}</h3>
                    <div className={styles.placeDescription}>
                      <p>{place.category}</p>
                      <p>{place.schedule}</p>
                      <p>{place.description}</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious className={styles.prevButton} />
        <CarouselNext className={styles.nextButton} />
      </Carousel>
    </section>
  );
}
