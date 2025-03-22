import React from 'react';
import styles from '@/components/cards/items/rating/rating.module.css';
import Image from 'next/image';
import Star from '@/public/icons/star.svg';
import { cn } from '@/lib/utils';

interface RatingProps {
  rating: number;
  className?: string;
}

export default function Rating({ rating, className }: RatingProps) {
  return (
    <div className={cn(styles.rating, className)}>
      <div className={styles.background}></div>
      <Image
        src={Star}
        alt={'Rating'}
        width={12}
        height={11}
        className={styles.star}
      />
      <p className={styles.value}>{rating}</p>
    </div>
  );
}
