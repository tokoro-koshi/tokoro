import { CheckCircle } from 'lucide-react';
import styles from './user-benefits.module.css';
import { benefits } from '@/lib/constants/landing/benefits';

export default function UserBenefits() {
  return (
    <section className={styles.benefits}>
      <h2 className={styles.title}>Why Choose Tokoro?</h2>
      <ul className={styles.list}>
        {benefits.map((benefit, index) => (
          <li key={index} className={styles.item}>
            <CheckCircle className={styles.icon} />
            <span>{benefit}</span>
          </li>
        ))}
      </ul>
    </section>
  );
}
