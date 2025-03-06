import { PlaceClient } from '@/lib/requests/place.client';
import Randomizer from '@/components/fyp/randomizer';
import Sections from '@/components/fyp/sections';
import { Place } from '@/lib/types/place';
import { FavoritesClient } from '@/lib/requests/favorites.client';

const sections = ['nearby', 'recommended', 'saved'] as const;

type ForYouProps = {
  searchParams: {
    section: string;
  };
};

export default async function ForYouPage({ searchParams }: ForYouProps) {
  const activeSection =
    sections.find((x) => searchParams['section']?.trim().toLowerCase() === x) ??
    sections[0];

  let places: Place[];
  switch (activeSection) {
    case 'nearby': // TODO
    case 'recommended':
      places = await PlaceClient.getRandomPlaces(15);
      break;
    case 'saved':
      const { payload: collections }
        = await FavoritesClient.getAllCurrentUserCollections();
      const placesIds = collections.reduce(
        (acc, collection) => acc.concat(collection.placesIds),
        [] as string[],
      )

      places = await PlaceClient.getPlacesByIdArray(placesIds);
      break;
  }

  return (
    <section className={'flex flex-col gap-6 pb-14'}>
      <Randomizer />
      <div className={'mx-9 h-2 flex-grow rounded-full bg-white'} />
      <Sections activeSection={activeSection} places={places} />
    </section>
  );
}
