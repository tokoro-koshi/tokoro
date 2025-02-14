import styles from './call-to-action.module.css';
import ExploreButton from '@/components/buttons/explore/explore-buttons';
import Image from 'next/image';
import Wave from '@/public/wave.svg';

export default function CallToAction() {
  return (
    <section className={styles.cta}>
      <Image src={Wave} alt={'Wave'} className={styles.wave} />
      <h2 className={styles.title}>Ready to Explore?</h2>
      <p className={styles.description}>
        Join Tokoro today and start discovering amazing places near you!
      </p>
      <ExploreButton />
    </section>
  );
}
