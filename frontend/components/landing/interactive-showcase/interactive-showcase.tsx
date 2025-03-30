import { Place } from '@/lib/types/place';
import styles from './interactive-showcase.module.css';
import PlaceCarousel from '@/components/cards/place-carousel/place-carousel';

interface InteractiveShowcaseProps {
  places: Place[];
}

export default function InteractiveShowcase({
  places,
}: InteractiveShowcaseProps) {
  return (
    <section className={styles.showcase} id='features'>
      <div className={styles.titleSection}>
        <div className={styles.line}></div>
        <h2 className={styles.title}>Explore Amazing Places</h2>
        <div className={styles.line}></div>
      </div>
      <PlaceCarousel places={places} className="!mx-0" />
    </section>
  );
}
