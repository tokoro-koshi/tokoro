import { steps } from '@/lib/constants/landing/steps';
import styles from './how-it-works.module.css';
import Image from 'next/image';
import Wave2 from '@/public/landing/wave-how-it-works.svg';
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from '@/components/ui/card';

export default function HowItWorks() {
  return (
    <section className={styles.howItWorks} id="about">
      <Image src={Wave2} alt={'Wave'} className={styles.wave} />
      <div className={styles.titleSection}>
        <div className={styles.line}></div>
        <h2 className={styles.title}>How It Works?</h2>
        <div className={styles.line}></div>
      </div>

      <div className={styles.stepsContainer}>
        {steps.map((step, index) => (
          <Card key={index} className={styles.stepCard}>
            <CardHeader>
              <CardTitle>{step.title}</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>{step.description}</CardDescription>
            </CardContent>
          </Card>
        ))}
      </div>
      <div className={styles.bottomLine}></div>
    </section>
  );
}
