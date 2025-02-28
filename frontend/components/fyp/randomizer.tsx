'use client';

import Image from 'next/image';
import { useQuery } from '@tanstack/react-query';
import { PlaceClient } from '@/lib/requests/place.client';
import Cubes from '@/public/icons/cubes.png';
import styles from './fyp.module.css';
import { Button } from '@/components/buttons';
import PlaceList from '@/components/generic/place-list/place-list';

export default function Randomizer() {
  const {
    data: places,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['randomPlaces'],
    queryFn: async () => await PlaceClient.getRandomPlaces(3),
    enabled: false,
  });

  return (
    <section className={styles.randomizer}>
      <div className={styles.title}>
        <h1>
          Don’t know where you want to go? It’s{' '}
          <span className={'text-white'}>not a problem!</span>
        </h1>
        <p>Just let our randomizer help you decide!</p>
      </div>
      {places ? (
        <PlaceList places={places} />
      ) : (
        <Image src={Cubes} alt={'Cubes'} className={styles.cubes} />
      )}
      <Button className={styles.button} action={refetch} disabled={isLoading}>
        {isLoading ? 'Loading...' : places ? 'Roll again' : 'Random'}
      </Button>
    </section>
  );
}
