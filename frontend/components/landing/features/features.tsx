import Image from 'next/image';
import { features } from '@/lib/constants/landing/features';
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from '@/components/ui/card';
import TopWave from '@/public/landing/wave-features.svg';
import styles from './features.module.css';

export default function Features() {
  return (
    <section className={styles.features} id={'features'}>
      <Image src={TopWave} alt={'Top wave'} className={styles.wave} />
      <ul className={styles.cardContainer}>
        {features.map((feature, index) => (
          <li key={index}>
            <Card className={styles.card}>
              <CardHeader>
                <Image
                  src={feature.icon}
                  alt={'Feature Icon'}
                  width={50}
                  height={60}
                  className={styles.icon}
                />
              </CardHeader>
              <CardContent className={styles.content}>
                <CardTitle>{feature.title}</CardTitle>
                <CardDescription>{feature.description}</CardDescription>
              </CardContent>
            </Card>
          </li>
        ))}
      </ul>
    </section>
  );
}
