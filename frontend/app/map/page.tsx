import GoogleMapComponent from '@/components/map/GoogleMap';
import { PlaceClient } from '@/lib/requests/place.client';

export default async function MapPage() {
  const places = await PlaceClient.getAllPlaces(0, 1000);
  const center = { lat: 49.841328512070056, lng: 24.02792769795437 };
  
  return (
      <div className="w-full h-svh pt-16 pb-2 md:py-24 lg:px-20">
        <div className="h-full w-full rounded-[48px] bg-foreground sm:p-3.5 relative">
          <GoogleMapComponent places={places} center={center} zoom={12} doesShowDetails={true} showsAll={true}/>
        </div>
      </div>
  );
}