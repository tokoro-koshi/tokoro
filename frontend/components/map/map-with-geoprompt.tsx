'use client';

import { useEffect, useState } from 'react';
import { Place } from '@/lib/types/place';
import GoogleMapComponent from '@/components/map/google-map';
import styles from '@/app/map/map.module.css';

export default function MapWithGeoPrompt({ places }: { places: Place[] }) {
  const [center, setCenter] = useState<{ lat: number; lng: number } | null>(null);
  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      (position) => setCenter({
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      }),
      (error) => {
        if (!(error instanceof GeolocationPositionError ) || error.code !== 1) {
          console.error('Error getting location:', error);
        }
        // Default location if geolocation fails
        setCenter({ lat: 49.841328512070056, lng: 24.02792769795437 });
      }
    )
  }, []);

  if (!center) {
    return (
      <div className={styles.pageContainer}>
        <div className={styles.mapWrapper}>
          Please give us permission to access your location.
        </div>
      </div>
    );
  }

  return (
    <div className={styles.pageContainer}>
      <div className={styles.mapWrapper}>
        <GoogleMapComponent
          places={places}
          center={center}
          zoom={12}
          doesShowDetails={true}
          showsAll={true}
        />
      </div>
    </div>
  );
}