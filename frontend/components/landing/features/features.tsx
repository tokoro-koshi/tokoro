import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription, CardContent,
} from '@/components/ui/card';
import styles from './features.module.css';
import { features } from '@/lib/constants/landing/features';
import Image from 'next/image';
import Wave from '@/public/wave-features.svg';

export default function Features() {
  return (
    <section className={styles.features} id={'features'}>
      <Image src={Wave} alt={'Wave'} className={styles.wave} />
      <div className={styles.cardContainer}>
        {features.map((feature, index) => (
          <Card key={index} className={styles.card}>
            <CardHeader>
              <Image
                src={feature.icon}
                alt={'/placeholder.svg'}
                width={50}
                height={60}
                className={styles.iconWrapper}
              />
            </CardHeader>
            <CardContent className={styles.content}>
              <CardTitle>{feature.title}</CardTitle>
              <CardDescription>{feature.description}</CardDescription>
            </CardContent>
          </Card>
        ))}
      </div>
    </section>
  );
}
