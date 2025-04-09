'use client';

import { useRouter } from 'next/navigation';
import { Place } from '@/lib/types/place';
import { cn } from '@/lib/utils';
import PlaceList from '@/components/cards/place-list/place-list';
import styles from './fyp.module.css';
import axios from 'axios';
import { BeatLoader } from 'react-spinners';
import { useUser } from '@/lib/stores/user';
import { useQuery } from '@tanstack/react-query';
import useGeolocation from '@/hooks/geolocation';

const sections = ['nearby', 'recommended', 'saved'] as const;

type SectionsProps = {
  activeSection: (typeof sections)[number];
};

export default function Sections({ activeSection }: SectionsProps) {
  const router = useRouter();

  // Obtain the user's favorite places IDs from metadata of a current user
  const user = useUser((state) => state.user);
  const favoritesIds = user?.userMetadata?.collections
    .flat()
    ?.map((item) => item.placesIds)
    ?.flat() 
    ?? [];

  // Get the user's geolocation
  const position = useGeolocation();

  // Fetch the relevant places based on the active section
  const { data: places } = useQuery({
    queryKey: ['savedPlaces', activeSection, position, ...favoritesIds],
    queryFn: async () => {
      if (activeSection === 'saved') {
        const ids = favoritesIds.join(',');
        const { data } = await axios.get<Place[]>(`/api/places/batch/${ids}`);
        return data;
      } else if (activeSection === 'nearby') {
        const { data } = await axios.get<Place[]>(`/api/places/nearby`, {
          params: position,
        });
        return data;
      } else {
        // TODO: recommended
        const { data } = await axios.get<Place[]>(`/api/places/random`);
        return data;
      }
    },
  });

  return (
    <div className={styles.sections}>
      <ul className={styles.sectionsList}>
        {sections.map((section) => (
          <li key={section}>
            <button
              onClick={() => router.push(`?section=${section}`)}
              className={cn(styles.button, {
                [styles.active]: activeSection === section,
              })}
              disabled={activeSection === section}
            >
              {section}
            </button>
          </li>
        ))}
      </ul>
      {activeSection === 'nearby' && !places ? (
        <div className={styles.searchingLabel}>
          <span>Searching for nearby places</span>
          <BeatLoader color={'white'} />
        </div>
      ) : (
        <div className={styles.sectionContent}>
          <PlaceList places={places} />
        </div>
      )}
    </div>
  );
}
