import Image from 'next/image'
import { Card, CardContent } from '@/components/ui/card'
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from '@/components/ui/carousel'
import styles from './Testimonials.module.css'
import { testimonials } from '@/lib/constants/landing/testimonials';

export default function Testimonials() {
  return (
    <section className={styles.testimonials}>
      <h2 className={styles.title}>What Our Users Say</h2>
      <Carousel className={styles.carousel}>
        <CarouselContent>
          {testimonials.map((testimonial, index) => (
            <CarouselItem key={index} className={styles.carouselItem}>
              <Card>
                <CardContent className={styles.cardContent}>
                  <Image src={testimonial.avatar || "/placeholder.svg"} alt={testimonial.name} width={64} height={64} className={styles.avatar} />
                  <p className={styles.comment}>&#34;{testimonial.comment}&#34;</p>
                  <p className={styles.name}>{testimonial.name}</p>
                </CardContent>
              </Card>
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious />
        <CarouselNext />
      </Carousel>
    </section>
  )
}

