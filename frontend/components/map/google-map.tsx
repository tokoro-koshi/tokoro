'use client';

import { useState, useMemo, useCallback } from 'react';
import {
  GoogleMap as GoogleMap,
  LoadScript,
  Marker,
} from '@react-google-maps/api';
import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/cards/place-card/place-card';
import { X } from 'lucide-react';
import { useRouter } from 'next/navigation';
import GoogleMaps from '@/public/icons/google-maps.svg';
import Image from 'next/image';
import styles from './google-map.module.css';

interface GoogleMapProps {
  places: Place[];
  center: { lat: number; lng: number };
  zoom: number;
  doesShowDetails?: boolean;
  showsAll?: boolean;
}

const GoogleMapComponent = ({
  places,
  center,
  zoom,
  doesShowDetails,
  showsAll,
}: GoogleMapProps) => {
  const router = useRouter();
  const [selectedPlace, setSelectedPlace] = useState<Place | null>(null);

  const handleMarkerClick = useCallback(
    (place: Place) => {
      if (doesShowDetails) {
        setSelectedPlace(place);
      } else {
        router.push(`/map/${place.id}`);
      }
    },
    [doesShowDetails, router]
  );

  const memoizedMarkers = useMemo(() => {
    return places.map((place) => (
      <Marker
        key={place.id}
        position={{ lat: place.location.coordinate.latitude, lng: place.location.coordinate.longitude }}
        onClick={() => handleMarkerClick(place)}
        icon='https://maps.google.com/mapfiles/ms/icons/red-dot.png'
      />
    ));
  }, [handleMarkerClick, places]);

  const apiKey = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY;

  if (!apiKey) {
    console.error('Google Maps API key is not provided');
    return <div className={styles.apiKeyError}>Unexpected error happened</div>;
  }

  return (
    <LoadScript googleMapsApiKey={apiKey}>
      <GoogleMap
        mapContainerStyle={{
          width: '100%',
          height: '100%',
          borderRadius: '40px',
        }}
        center={center}
        zoom={zoom}
        options={{
          fullscreenControl: false,
          streetViewControl: false,
          mapTypeControl: false,
          zoomControl: false,
          rotateControl: false,
          scaleControl: false,
          styles: [
            {
              featureType: 'poi',
              elementType: 'labels',
              stylers: [{ visibility: 'off' }],
            },
            {
              featureType: 'transit',
              elementType: 'labels',
              stylers: [{ visibility: 'off' }],
            },
            {
              featureType: 'road',
              elementType: 'labels',
              stylers: [{ visibility: 'off' }],
            },
          ],
        }}
      >
        {memoizedMarkers}
      </GoogleMap>

      {selectedPlace && (
        <div className={styles.selectedPlace}>
          <PlaceCard place={selectedPlace} />
          <a
            className={styles.googleMapsLink}
            target='_blank'
            href={`https://maps.google.com/maps?q=${selectedPlace.name}&ll=${selectedPlace.location.coordinate.latitude},${selectedPlace.location.coordinate.longitude}`}
          >
            <Image
              src={GoogleMaps}
              alt={'Google Maps'}
              width={40}
              height={40}
            />
          </a>
          <button
            className={styles.closeButton}
            onClick={() => setSelectedPlace(null)}
          >
            <X size={32} />
          </button>
        </div>
      )}

      {!showsAll && (
        <button
          className={styles.showAllButton}
          onClick={() => router.push('/map')}
        >
          Show all
        </button>
      )}
    </LoadScript>
  );
};

export default GoogleMapComponent;
