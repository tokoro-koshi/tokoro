import Header from '@/components/landing/header/header';
import Hero from '@/components/landing/hero/hero';
import Features from '@/components/landing/features/features';
import InteractiveShowcase from '@/components/landing/interactive-showcase/interactive-showcase';
import HowItWorks from '@/components/landing/how-it-works/how-it-works';
import Testimonials from '@/components/landing/testimonials/testimonials';
import CallToAction from '@/components/landing/call-to-action/call-to-action';
import { PlaceClient } from '@/lib/requests/place.client';

export default async function Home() {
  const places = await PlaceClient.getRandomPlaces(20);

  return (
    <main className='flex-grow bg-primary'>
      <Header />
      <Hero />
      <Features />
      <InteractiveShowcase places={places} />
      <HowItWorks />
      <Testimonials />
      <CallToAction />
    </main>
  );
}