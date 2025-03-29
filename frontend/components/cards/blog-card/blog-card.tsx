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
            } gap-6 md:gap-8 ${className}`}
            {...props}
            onClick={onClick}
            role="button"
            tabIndex={0}
        >
            <div className={`flex-shrink-0 ${
                imagePosition === 'left'
                    ? 'md:-ml-16'
                    : 'md:-mr-16'
            } md:-mb-8`}>
                <Image
                    src={imageUrl}
                    alt={title}
                    width={219}
                    height={219}
                    className="rounded-[43px] cursor-pointer"
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