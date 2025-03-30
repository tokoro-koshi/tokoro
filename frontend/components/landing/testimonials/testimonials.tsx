import Image from 'next/image';
import { Card, CardContent } from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import styles from './testimonials.module.css';
import { testimonials } from '@/lib/constants/landing/testimonials';
import routes from '@/lib/constants/routes';

export default function Testimonials() {
  return (
    <section className={styles.testimonials}>
      <Carousel className={styles.carousel}>
        <CarouselContent>
          {testimonials.map((testimonial, index) => (
            <CarouselItem key={index} className={styles.carouselItem}>
              <Card>
                <CardContent className={styles.cardContent}>
                  <Image
                    src={testimonial.avatar ?? routes.placeholder}
                    alt={testimonial.name}
                    width={50}
                    height={50}
                    className={styles.avatar}
                  />
                  <p className={styles.comment}>
                    &#34;{testimonial.comment}&#34;
                  </p>
                  <p className={styles.name}>{testimonial.name}</p>
                </CardContent>
              </Card>
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious className={'max-sm:hidden'} />
        <CarouselNext className={'max-sm:hidden'} />
      </Carousel>
    </section>
  );
}
