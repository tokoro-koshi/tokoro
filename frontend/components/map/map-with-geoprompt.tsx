'use client';

import { Place } from '@/lib/types/place';
import GoogleMapComponent from '@/components/map/google-map';
import styles from '@/app/map/map.module.css';
import useGeolocation from '@/hooks/geolocation';

export default function MapWithGeoPrompt({ places }: { places: Place[] }) {
  const center = useGeolocation();

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
