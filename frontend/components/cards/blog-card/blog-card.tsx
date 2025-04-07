import Image, { StaticImageData } from 'next/image';
import styles from './blog-card.module.css';

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
        className={`mx-auto flex-shrink-0 md:mx-0 ${
          imagePosition === 'left' ? 'md:-ml-16' : 'md:-mr-16'
        } w-full md:-mb-8 md:w-auto`}
      >
        <Image
          src={imageUrl}
          alt={title}
          width={219}
          height={219}
          className='h-[219px] w-full cursor-pointer rounded-[43px] object-cover md:w-[219px]'
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
