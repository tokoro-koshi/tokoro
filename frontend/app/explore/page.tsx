import Randomizer from '@/components/fyp/randomizer';
import Sections from '@/components/fyp/sections';

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
  return (
    <section className={'flex flex-col gap-6 pb-14 pt-16'}>
      <Randomizer />
      <div className={'mx-9 h-2 flex-grow rounded-full bg-white'} />
      <Sections activeSection={activeSection} />
    </section>
  );
}
