import Hero from '@/components/landing/hero/hero';
import Features from '@/components/landing/features/features';
import InteractiveShowcase from '@/components/landing/interactive-showcase/interactive-showcase';
import UserBenefits from '@/components/landing/user-benefits/user-benefits';
import HowItWorks from '@/components/landing/how-it-works/how-it-works';
import Testimonials from '@/components/landing/testimonials/testimonials';
import CallToAction from '@/components/landing/call-to-action/call-to-action';

export default async function Home() {
  return (
    <>
      <Hero />
      <Features />
      <InteractiveShowcase />
      <UserBenefits />
      <HowItWorks />
      <Testimonials />
      <CallToAction />
    </>
  );
}
