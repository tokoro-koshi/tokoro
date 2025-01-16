import { Button } from '@/components/ui/button';
import styles from './Hero.module.css';

export default function Hero() {
  return (
    <section className={styles.hero}>
      <h1 className={styles.title}>Discover Amazing Places Near You</h1>
      <p className={styles.subtitle}>
        Find interesting places to visit based on your preferences with ease.
      </p>
      <Button size="lg" className={styles.cta}>Start Exploring</Button>
    </section>
  )
}

