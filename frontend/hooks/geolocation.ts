import { useEffect, useState } from 'react';

/**
 * Interface representing the geolocation of a user.
 */
export interface Geolocation {
  // Latitude of the user's location.
  lat: number;

  // Longitude of the user's location.
  lng: number;
}

/**
 * A hook to get the user's browser geolocation.
 */
export default function useGeolocation(): Geolocation {
  const [position, setPosition] = useState<Geolocation>(
    // Default to Lviv, Ukraine
    { lat: 49.841328512070056, lng: 24.02792769795437 }
  );

  // Query the user's geolocation on mount
  useEffect(() => {
    // Check if geolocation is available in the browser
    if (!('geolocation' in navigator)) return;

    // Request the user's current position
    navigator.geolocation.getCurrentPosition(
      ({ coords }) => {
        const newPos = { lat: coords.latitude, lng: coords.longitude };
        setPosition(
          (prev) => prev.lat === newPos.lat && prev.lng === newPos.lng 
            ? prev 
            : newPos
        );
      },
      console.error
    );
  }, []);

  return position;
}
