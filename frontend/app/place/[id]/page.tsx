import { PlaceClient } from '@/lib/requests/place.client';
import { redirect } from 'next/navigation';

type PlacePageProps = {
  params: {
    id: string;
  }
}

export default async function PlacePage({ params }: PlacePageProps) {
  const place = await PlaceClient.getPlaceById(params.id),
        lat = place.location.coordinate.latitude,
        lon = place.location.coordinate.longitude;

  redirect(`https://maps.google.com/maps?q=${place.name}&ll=${lat},${lon}`);
}
