import GoogleMapComponent from '@/components/map/GoogleMap';
import { PlaceClient } from '@/lib/requests/place.client';
import styles from './map.module.css';

export default async function MapPage() {
  const places = await PlaceClient.getAllPlaces(0, 1000);
  const center = { lat: 49.841328512070056, lng: 24.02792769795437 };
  
  return (
    <div className={styles.pageContainer}>
      <div className={styles.mapWrapper}>
        <GoogleMapComponent places={places} center={center} zoom={12} doesShowDetails={true} showsAll={true} />
      </div>
    </div>
  );
}