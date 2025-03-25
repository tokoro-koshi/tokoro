import GoogleMapComponent from '@/components/map/GoogleMap';
import { PlaceClient } from '@/lib/requests/place.client';

export default async function PlaceMapPage({params}: {params: {id: string}}) {
  const place = await PlaceClient.getPlaceById(params.id);
  const center = { lat: place.location.coordinate.latitude, lng: place.location.coordinate.longitude };

  return (
    <div className="w-full h-svh pt-16 pb-2 md:py-24 lg:px-20">
      <div className="h-full w-full rounded-[48px] bg-foreground sm:p-3.5 relative">
        <GoogleMapComponent places={[place]} center={center} zoom={16} doesShowDetails={true} />
      </div>
    </div>
  );
}