import Image from 'next/image';
import { Card, CardContent } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import styles from './interactive-showcase.module.css';
import { places } from '@/lib/constants/landing/interactive-showcase';

export default function InteractiveShowcase() {
  return (
    <section className={styles.showcase}>
      <h2 className={styles.title}>Explore Amazing Places</h2>
      <Carousel className={styles.carousel}>
        <CarouselContent>
          {places.map((place, index) => (
            <CarouselItem key={index} className={styles.carouselItem}>
              <Card>
                <CardContent className={styles.cardContent}>
                  <Image
                    src={place.image || '/placeholder.svg'}
                    alt={place.name}
                    width={300}
                    height={200}
                    className={styles.image}
                  />
                  <h3 className={styles.placeName}>{place.name}</h3>
                  <p className={styles.category}>{place.category}</p>
                </CardContent>
              </Card>
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious />
        <CarouselNext />
      </Carousel>
    </section>
  );
}
