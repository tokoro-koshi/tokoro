'use client';

import { useRouter } from 'next/navigation';
import Image from 'next/image';
import routes from '@/lib/constants/routes';
import { ExploreButton } from '@/components/buttons';
import { Button } from '@/components/ui/button';
import Place from '@/public/landing/place.avif';
import styles from './hero.module.css';

export default function Hero() {
  const router = useRouter();
  return (
    <section className={styles.hero}>
      <div className={styles.text}>
        <h2 className={styles.title}>
          Discover Amazing <span className={styles.highlight}>Places</span> Near
          You
        </h2>
        <p className={styles.subtitle}>
          Find interesting places to visit based on your preferences with ease.
        </p>
      </div>
      <div className={styles.buttons}>
        <ExploreButton />
        <Button
          className={styles.btn}
          size={'lg'}
          onClick={() => router.push(routes.about)}
        >
          Learn more
        </Button>
      </div>
      <div className={styles.myImage}>
        <Image
          src={Place}
          alt={'Places'}
          className={styles.place}
          quality={100}
          loading={'eager'}
        />
      </div>
    </section>
  );
}
