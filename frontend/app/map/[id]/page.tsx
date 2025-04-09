import GoogleMapComponent from '@/components/map/google-map';
import { PlaceClient } from '@/lib/requests/place.client';
import styles from '../map.module.css';
import { notFound } from 'next/navigation';

export default async function PlaceMapPage({
  params,
}: {
  params: { id: string };
}) {
  const place = await PlaceClient.getPlaceById(params.id);
  if (!place || !place.location) notFound();

  const center = {
    lat: place.location.coordinate.latitude,
    lng: place.location.coordinate.longitude,
  };
  return (
    <div className={styles.pageContainer}>
      <div className={styles.mapWrapper}>
        <GoogleMapComponent
          places={[place]}
          center={center}
          zoom={16}
          doesShowDetails={true}
        />
      </div>
    </div>
  );
}
