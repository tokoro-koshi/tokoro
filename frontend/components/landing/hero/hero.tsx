import { Button } from '@/components/ui/button';
import styles from './hero.module.css';
import Image from 'next/image';
import Place from '@/public/place.png';
import ExploreButton from '@/components/buttons/explore/explore-buttons';

export default function Hero() {
  return (
    <section className={styles.hero}>
      <div className={styles.content}>
        <div className={styles.container}>
          <h2 className={styles.title}>
            Discover Amazing
            <span className={styles.highlight}>Places</span>
            Near You
          </h2>
          <p className={styles.subtitle}>
            Find interesting places to visit based on your preferences with
            ease.
          </p>
          <ExploreButton />
          <Button size={'lg'} className={styles.btn}>
            Learn more
          </Button>
        </div>
        <div className={styles.image}></div>
      </div>
      <div className={styles.myImage}>
        <Image src={Place} alt={'Places'} className={styles.place} quality={100} />
      </div>
    </section>
  );
}
