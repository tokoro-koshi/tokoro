import { steps } from '@/lib/constants/landing/steps';
import styles from './how-it-works.module.css';

export default function HowItWorks() {
  return (
    <section className={styles.howItWorks}>
      <h2 className={styles.title}>How It Works</h2>
      <div className={styles.stepsContainer}>
        {steps.map((step, index) => (
          <div key={index} className={styles.step}>
            <div className={styles.iconWrapper}>{step.icon}</div>
            <h3 className={styles.stepTitle}>{step.title}</h3>
            <p className={styles.stepDescription}>{step.description}</p>
          </div>
        ))}
      </div>
    </section>
  );
}
