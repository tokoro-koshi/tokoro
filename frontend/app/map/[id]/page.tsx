import GoogleMapComponent from '@/components/map/GoogleMap';
import { PlaceClient } from '@/lib/requests/place.client';
import styles from '../map.module.css';

export default async function PlaceMapPage({params}: {params: {id: string}}) {
  const place = await PlaceClient.getPlaceById(params.id);
  const center = { lat: place.location.coordinate.latitude, lng: place.location.coordinate.longitude };

  return (
    <div className={styles.pageContainer}>
      <div className={styles.mapWrapper}>
        <GoogleMapComponent places={[place]} center={center} zoom={16} doesShowDetails={true} />
      </div>
    </div>
  );
}