import { Card, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import styles from './Features.module.css'
import { features } from '@/lib/constants/landing/features';

export default function Features() {
  return (
    <section className={styles.features} id="features">
      <h2 className={styles.title}>Key Features</h2>
      <div className={styles.cardContainer}>
        {features.map((feature, index) => (
          <Card key={index} className={styles.card}>
            <CardHeader>
              <div className={styles.iconWrapper}>{feature.icon}</div>
              <CardTitle>{feature.title}</CardTitle>
              <CardDescription>{feature.description}</CardDescription>
            </CardHeader>
          </Card>
        ))}
      </div>
    </section>
  )
}

