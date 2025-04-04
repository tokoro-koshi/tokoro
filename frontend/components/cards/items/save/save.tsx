﻿'use client';
import React, { useEffect, useState } from 'react';
import { Button } from '@/components/ui/button';
import { Bookmark } from 'lucide-react';
import { cn } from '@/lib/utils';
import styles from './save.module.css';
import axios from 'axios';
import { useUser } from '@/lib/stores/user';
import { ClipLoader } from 'react-spinners';

interface SaveButtonProps {
  placeId: string;
  userId?: string;
  className?: string;
  variant: 'dark' | 'light';
}

export default function SaveButton({
  placeId,
  className,
  variant,
}: SaveButtonProps) {
  const { user } = useUser();
  const [isChecked, setIsChecked] = useState<boolean | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (user && isChecked === null) {
      const isFavorite =
        user.userMetadata?.collections.some((collection) =>
          collection.placesIds.includes(placeId)
        ) ?? false;
      setIsChecked(isFavorite);
    }
  }, [placeId, user, isChecked]);

  const handleSave = async (event: React.MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    if (isLoading) return;
    setIsLoading(true);
    setIsChecked((prev) => !prev);
    await axios.post('/api/places/toggle-favorite', { placeId });
    setIsLoading(false);
  };

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
      onClick={handleSave}
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
