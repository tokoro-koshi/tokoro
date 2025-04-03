'use client';

import { useRouter } from 'next/navigation';
import { Place } from '@/lib/types/place';
import { cn } from '@/lib/utils';
import PlaceList from '@/components/cards/place-list/place-list';
import styles from './fyp.module.css';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { BeatLoader, ClipLoader } from 'react-spinners';

const sections = ['nearby', 'recommended', 'saved'] as const;

type SectionsProps = {
  places: Place[];
  activeSection: (typeof sections)[number];
};

export default function Sections({ places: serverPlaces, activeSection }: SectionsProps) {
  const [places, setPlaces] = useState<Place[]>(serverPlaces);

  useEffect(() => {
    const fetchPlaces = async (latitude: number, longitude: number) => {
      try {
        const response = await axios.post(`/api/places/nearby`, { latitude, longitude });
        console.log(response.data);
        setPlaces(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    setPlaces(serverPlaces);
    
    if (activeSection !== 'nearby') return;
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
          };
          fetchPlaces(latitude, longitude);
        },
        (error) => console.error(error),
      );
    }
  }, [activeSection, serverPlaces]);
  
  const router = useRouter();
  return (
    <div className={styles.sections}>
      <ul className={styles.sectionsList}>
        {sections.map((section) => (
          <li key={section}>
            <button
              onClick={() => router.push(`?section=${section}`)}
              className={cn(styles.button, {
                [styles.active]: activeSection === section,
              })}
              disabled={activeSection === section}
            >
              {section}
            </button>
          </li>
        ))}
      </ul>
      {activeSection === 'nearby' && places.length === 0 
        ?
        <div className={styles.searchingLabel}>
          <span>Searching for nearby places</span>
          <BeatLoader
            color={'white'}
          />
        </div> 
        :
        <div className={styles.sectionContent}>
          <PlaceList places={places} />
        </div>}
    </div>
  );
}
