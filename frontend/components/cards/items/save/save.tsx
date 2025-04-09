'use client';

import { useMemo } from 'react';
import { Button } from '@/components/ui/button';
import { Bookmark } from 'lucide-react';
import { cn } from '@/lib/utils';
import styles from './save.module.css';
import axios from 'axios';
import { useUser } from '@/lib/stores/user';
import { ClipLoader } from 'react-spinners';
import { useMutation } from '@tanstack/react-query';
import { FavoritesCollection } from '@/lib/types/place';

interface SaveButtonProps {
  placeId: string;
  className?: string;
  variant: 'dark' | 'light';
}

export default function SaveButton({
  placeId,
  className,
  variant,
}: SaveButtonProps) {
  const { user, setUser } = useUser();

  const isChecked = useMemo(() => {
    if (!user || !user.userMetadata) return false;
    return user.userMetadata.collections.some((collection) =>
      collection.placesIds.includes(placeId)
    );
  }, [placeId, user]);

  const { mutate: handleSave, isPending: isLoading } = useMutation({
    mutationKey: ['savePlace'],
    mutationFn: async () => {
      const { data } = await axios.post<FavoritesCollection[]>(
        '/api/places/toggle-favorite',
        { placeId }
      );
      return data;
    },
    onSuccess: (data) => {
      setUser({
        ...user!,
        userMetadata: {
          ...(user?.userMetadata ?? {}),
          collections: data,
        },
      });
    },
  });

  if (isChecked === null) {
    return null;
  }

  if (isLoading) {
    return (
      <div
        className={cn(
          styles.button,
          className,
          variant === 'dark' ? styles.buttonDark : styles.buttonLight
        )}
      >
        <ClipLoader
          color={variant === 'dark' ? 'var(--foreground)' : 'var(--background)'}
          className={cn(
            styles.icon,
            isChecked &&
              (variant === 'dark' ? styles.fillDark : styles.fillLight)
          )}
        />
      </div>
    );
  }

  return (
    <Button
      disabled={isLoading}
      className={cn(
        styles.button,
        className,
        variant === 'dark' ? styles.buttonDark : styles.buttonLight
      )}
      onClick={() => handleSave()}
    >
      <Bookmark
        className={cn(
          styles.icon,
          isChecked && (variant === 'dark' ? styles.fillDark : styles.fillLight)
        )}
      />
    </Button>
  );
}
