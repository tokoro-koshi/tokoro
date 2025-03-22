"use client";
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
  variant: "dark" | "light";
}

export default function SaveButton({ placeId, className, variant }: SaveButtonProps) {
  const {user} = useUser();
  const [isChecked, setIsChecked] = useState<boolean|null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // const { mutate } = useMutation({
  //   mutationFn: async () => {
  //     if (!user || isChecked !== null) return;
  //     const { data } = await axios.post<Record<"isFavorite",boolean>>('/api/places/is-favorite', {placeId})
  //     return data;
  //   },
  //   onSuccess: (res) => {
  //     setIsChecked(res?.isFavorite ?? false);
  //     setIsLoading(false);
  //   },
  //   mutationKey: ['isFavorite', placeId, user?.userId, isChecked],
  // });


  // useEffect(() => {
  //   if (!isUserLoading && user) {
  //     mutate();
  //   }
  // }, [isUserLoading, user, isChecked, mutate]);

  useEffect(() => {
    if(user && isChecked === null) {
      const isFavorite = user.userMetadata?.collections.some((collection) => collection.placesIds.includes(placeId)) ?? false;
      setIsChecked(isFavorite);
    }
  }, [placeId, user, isChecked]);

  if (isChecked === null) {
    return null;
  }

  const handleSave = async () => {
    if (isLoading) return;
    setIsLoading(true);
    setIsChecked(!isChecked);
    await axios.post('/api/places/toggle-favorite', {placeId});
    // await axios.post('/api/auth/me', {placeId});
    setIsLoading(false);
  }

  return (
    !isLoading ?
      <Button
        disabled={isLoading}
        className={cn(styles.button, className, variant === 'dark' ? styles.buttonDark : styles.buttonLight)}
        onClick={handleSave}
      >
        <Bookmark className={cn(styles.icon, isChecked && (variant === 'dark' ? styles.fillDark : styles.fillLight))} />
      </Button>:
      <div className={cn(styles.button, className, variant === 'dark' ? styles.buttonDark : styles.buttonLight)}>
        <ClipLoader color={variant === 'dark' ? 'var(--foreground)' : 'var(--background)'} className={cn(styles.icon, isChecked && (variant === 'dark' ? styles.fillDark : styles.fillLight))}/>
      </div>
  );
}
