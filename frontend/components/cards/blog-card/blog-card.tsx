import Image, { StaticImageData } from 'next/image';
import styles from './blog-card.module.css';
import { cn } from '@/lib/utils';

interface BlogCardProps {
  imageUrl: StaticImageData | string;
  title: string;
  description: string;
  date: string;
  imagePosition?: 'left' | 'right';
  className?: string;
  onClick?: () => void;
}

export const BlogCard = ({
  imageUrl,
  title,
  description,
  date,
  imagePosition = 'left',
  className = '',
  onClick,
  ...props
}: BlogCardProps) => {
  return (
    <article
      className={`flex flex-col ${
        imagePosition === 'left' ? 'md:flex-row' : 'md:flex-row-reverse'
      } gap-4 md:gap-8 ${className}`}
      {...props}
      onClick={onClick}
      role='button'
      tabIndex={0}
    >
      <div
        className={cn(
          'mx-auto w-full flex-shrink-0 md:mx-0 md:-mb-8 md:w-auto',
          imagePosition === 'left' ? 'md:-ml-16' : 'md:-mr-16'
        )}
      >
        <Image
          src={imageUrl}
          alt={title}
          width={219}
          height={219}
          className={styles.image}
        />
      </div>

      <div className={styles.content}>
        <h3 className={styles.title}>{title}</h3>
        <div className={styles.date}>
          <time>{date}</time>
        </div>

        <p className={styles.description}>{description}</p>
      </div>
    </article>
  );
};
