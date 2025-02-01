import { Button } from '@/components/ui/button';
import styles from './CallToAction.module.css';

export default function CallToAction() {
  return (
    <section className={styles.cta}>
      <h2 className={styles.title}>Ready to Explore?</h2>
      <p className={styles.description}>
        Join Tokoro today and start discovering amazing places near you!
      </p>
      <Button size='lg' className={styles.button}>
        Sign Up Now
      </Button>
    </section>
  );
}
