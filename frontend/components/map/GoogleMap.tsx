"use client";
import { useState } from "react";
import { GoogleMap as GoogleMap, LoadScript, Marker } from '@react-google-maps/api';
import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/cards/place-card/place-card';
import { X } from 'lucide-react';
import { useRouter } from 'next/navigation';
import GoogleMaps from '@/public/icons/google-maps.svg';
import Image from 'next/image';

const mapContainerStyle = {
  width: "100%",
  height: "100%",
  borderRadius: "40px",
};

interface GoogleMapProps {
  places: Place[];
  center: { lat: number; lng: number };
  zoom: number;
  doesShowDetails?: boolean;
  showsAll?: boolean;
}

const GoogleMapComponent = ({ places, center, zoom, doesShowDetails, showsAll }: GoogleMapProps) => {
  const router = useRouter();
  const [selectedPlace, setSelectedPlace] = useState<Place | null>(null);
  const handleMarkerClick = (place: Place) => {
    if(doesShowDetails) {
      setSelectedPlace(place);
    }
    else {
      router.push(`/map/${place.id}`);
    }
  };

  return (
    <LoadScript googleMapsApiKey={process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY ?? ""}>
      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        center={center}
        zoom={zoom}
        options={{
          fullscreenControl: false, // Hide fullscreen button
          streetViewControl: false, // Hide "guy" (Street View)
          mapTypeControl: false, // Hide satellite/default switch
          zoomControl: false, // Hide zoom buttons
          rotateControl: false, // Hide rotate button
          scaleControl: false, // Hide scale bar
          // disableDefaultUI: true, // Hide everything
          styles: [
            {
              featureType: "poi", // Hides points of interest (restaurants, shops, etc.)
              elementType: "labels",
              stylers: [{ visibility: "off" }],
            },
            {
              featureType: "transit", // Hides transit stations
              elementType: "labels",
              stylers: [{ visibility: "off" }],
            },
            {
              featureType: "road", // Hides road labels (optional)
              elementType: "labels",
              stylers: [{ visibility: "off" }],
            },
          ],
        }}
      >
        {places.map((place) => (
          <Marker
            key={place.id}
            position={{ lat: place.location.coordinate.latitude, lng: place.location.coordinate.longitude }}
            onClick={() => handleMarkerClick({ ...place })}
            icon="https://maps.google.com/mapfiles/ms/icons/red-dot.png"
          />
        ))}
      </GoogleMap>

      {selectedPlace &&
        <div className="absolute right-6 top-6 w-64">
          <PlaceCard place={selectedPlace}/>
          <a className="absolute top-44 translate-y-1/2 right-2 p-0.5 rounded-lg bg-white" target="_blank" href={`https://maps.google.com/maps?q=${selectedPlace.name}&ll=${selectedPlace.location.coordinate.latitude},${selectedPlace.location.coordinate.longitude}`}>
            <Image src={GoogleMaps} alt={'Google Maps'} width={40} height={40}/>
          </a>
          <button className="absolute top-4 right-4 rounded-full text-foreground bg-background" onClick={() => setSelectedPlace(null)}><X size={32}/></button>
        </div>
      }

      {!showsAll && <button className="absolute top-2 left-0 -translate-x-1/3 -translate-y-1/2 bg-background rounded-3xl p-2 font-bold border-8 border-foreground text-xl" onClick={()=>router.push("/map")}>Show all</button>}

    </LoadScript>
  );
};

export default GoogleMapComponent;