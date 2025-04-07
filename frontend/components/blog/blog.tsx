'use client';
import styles from './blog.module.css';
import { Search, Send } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import Image from 'next/image';
import SaveButton from '@/components/cards/items/save/save';
import { BlogCard } from '@/components/cards/blog-card/blog-card';
import { blogPosts } from '@/lib/constants/blog/posts';
import { Place } from '@/lib/types/place';
import { useRouter } from 'next/navigation';

import Link from 'next/link';
import routes from '@/lib/constants/routes';

interface BlogProps {
  places: Place[];
}

export default function BlogComponent({ places }: BlogProps) {
  const router = useRouter();

  return (
    <div className={styles.blog}>
      <div className={styles.placeSection}>
        <div className={styles.inputArea}>
          <div className={styles.inputContainer}>
            <div className={styles.inputIcon}>
              <Search className='h-5 w-5' />
            </div>
            <Input
              placeholder='Type a name of article'
              className={styles.inputField}
            />
            <Button size='icon' className={styles.sendButton}>
              <Send className='h-5 w-5' />
            </Button>
          </div>
        </div>

        <div className={styles.topPlaces}>
          <h2 className={styles.header}>TOP 5 PLACES OF THE WEEK</h2>
          <div className={styles.places}>
            {places.map((place, index) => (
              <Link
                key={place.id}
                href={routes.place + '/' + place.id}
                target='_blank'
                className={styles.placeItem}
              >
                <div className={styles.placeNumber}>{index + 1}.</div>
                <div className={styles.placeImage}>
                  <Image
                    src={place.pictures[0] || '/placeholder.svg'}
                    alt={place.name}
                    width={91}
                    height={91}
                    className={styles.image}
                  />
                </div>
                <div className={styles.placeDetails}>
                  <h3 className={styles.placeName}>{place.name}</h3>

                  <p className={styles.placeLocation}>
                    {place.location.city}, {place.location.address}
                  </p>
                </div>
                <SaveButton
                  className={styles.saveButton}
                  placeId={place.id}
                  variant={'light'}
                />
              </Link>
            ))}
          </div>
        </div>
      </div>

      <div className={styles.blogPosts}>
        {blogPosts.map((post, index) => (
          <BlogCard
            key={post.id}
            imageUrl={post.imageUrl || 'placeholder.svg'}
            title={post.title}
            description={post.description}
            date={post.date}
            imagePosition={index % 2 === 0 ? 'left' : 'right'}
            className={styles.blogCard}
            onClick={() => {
              router.push(`/blog/${post.id}`);
            }}
          />
        ))}
      </div>
    </div>
  );
}
