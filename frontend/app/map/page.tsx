import { PlaceClient } from '@/lib/requests/place.client';
import MapWithGeoPrompt from '@/components/map/map-with-geoprompt';

// Disable caching because of fetching that uses cookies
export const dynamic = 'force-dynamic';

export default async function MapPage() {
  const places = await PlaceClient.getAllPlaces(0, 1000);
  return <MapWithGeoPrompt places={places} />;
}
