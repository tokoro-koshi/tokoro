"use client";
import React, { useEffect, useState } from 'react';
import { Button } from '@/components/ui/button';
import { Bookmark } from 'lucide-react';
import { cn } from '@/lib/utils';
import styles from './save.module.css';
import axios from 'axios';
import { useUser } from '@auth0/nextjs-auth0/client';

interface SaveButtonProps {
  placeId: string;
  userId?: string;
  className?: string;
  variant: "dark" | "light";
}

export default function SaveButton({ placeId, className, variant }: SaveButtonProps) {
  const {user, isLoading:isUserLoading} = useUser();
  const [isChecked, setIsChecked] = useState<boolean|null>(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (!user || isChecked !== null) return;
    axios.post('/api/places/is-favorite', {placeId}).then((res) => {
      console.log("Is saved",res.data);
      setIsChecked(res.data.isFavorite);
    });
    return;
  }, [user]);

  if (!isUserLoading && !user) {
    return null;
  }
  
  const handleSave = async () => {
    if (isLoading) return;
    setIsLoading(true);
    setIsChecked(!isChecked);
    await axios.post('/api/places/add-favorite', {placeId});
    setIsLoading(false);
  }

  return (
    <Button
      disabled={isLoading}
      className={cn(styles.button, className, isLoading && "opacity-30", variant === "dark" ? styles.buttonDark : styles.buttonLight)}
      onClick={handleSave}
    >
      <Bookmark className={cn(styles.icon, isChecked && (variant === "dark" ? styles.fillDark : styles.fillLight))} />
    </Button>
  );
}
