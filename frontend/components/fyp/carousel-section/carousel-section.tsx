import styles from './carousel-section.module.css';
import { Card, CardContent } from '@/components/ui/card';
import {
    Carousel,
    CarouselContent,
    CarouselItem,
} from '@/components/ui/carousel';
import Star from '@/public/star.svg';
import Image from "next/image";
import {Place} from "@/lib/types/place";

export interface CarouselSectionProps {
    title: string,
    places: Place[]
}

export default function CarouselSection({title, places}: CarouselSectionProps) {
    return (
        <section className={styles.savedPlace}>
            <div className={styles.line}></div>
            <h1 className={styles.title}>{title}</h1>
            <Carousel className={styles.carousel}>
                <CarouselContent>
                    {places.map((place, index) => (
                        <CarouselItem key={index} className={styles.carouselItem}>
                            <Card>
                                <CardContent className={styles.cardContent}>
                                    <div className={styles.ratingBackground}>
                                        <Image
                                            src={Star}
                                            alt='Rating'
                                            width={12}
                                            height={11}
                                            className={styles.star}
                                        />
                                        <p className={styles.rating}>{place.rating}</p>
                                    </div>
                                    <Image
                                        src={place.pictures[0] || '/placeholder.svg'}
                                        alt={place.name}
                                        width={350}
                                        height={195}
                                        className={styles.image}
                                    />
                                    <div className={styles.text}>
                                        <h3 className={styles.placeName}>{place.name}</h3>
                                        <div className={styles.placeDescription}>
                                            <p>{place.categoryId}</p>
                                            <p>{place.location.address}</p>
                                            <p className={styles.description}>{place.description}</p>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </CarouselItem>
                    ))}
                </CarouselContent>
            </Carousel>
        </section>
    );
}