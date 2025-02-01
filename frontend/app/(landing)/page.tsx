import Hero from '@/components/landing/hero/Hero';
import Features from '@/components/landing/features/Features';
import InteractiveShowcase from '@/components/landing/interactive-showcase/InteractiveShowcase';
import UserBenefits from '@/components/landing/user-benefits/UserBenefits';
import HowItWorks from '@/components/landing/how-it-works/HowItWorks';
import Testimonials from '@/components/landing/testimonials/Testimonials';
import CallToAction from '@/components/landing/call-to-action/CallToAction';

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
