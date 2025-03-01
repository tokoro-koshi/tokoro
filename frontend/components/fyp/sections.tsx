'use client';

import { useRouter } from 'next/navigation';
import { Place } from '@/lib/types/place';
import { cn } from '@/lib/utils';
import PlaceList from '@/components/cards/place-list/place-list';
import styles from './fyp.module.css';

const sections = ['nearby', 'recommended', 'saved'] as const;

type SectionsProps = {
  places: Place[];
  activeSection: typeof sections[number];
};

export default function Sections({ places, activeSection }: SectionsProps) {
  const router = useRouter();
  return (
    <div className={styles.sections}>
      <ul className={styles.sectionsList}>{
        sections.map((section) => (
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
        ))
      }</ul>
      <div className={styles.sectionContent}>
        <PlaceList places={places} />
      </div>
    </div>
  )
}
