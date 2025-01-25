import Header from '@/components/layout/header/Header';
import Hero from '@/components/landing/hero/Hero';
import Features from '@/components/landing/features/Features';
import InteractiveShowcase from '@/components/landing/interactive-showcase/InteractiveShowcase';
import UserBenefits from '@/components/landing/user-benefits/UserBenefits';
import HowItWorks from '@/components/landing/how-it-works/HowItWorks';
import Testimonials from '@/components/landing/testimonials/Testimonials';
import CallToAction from '@/components/landing/call-to-action/CallToAction';
import Footer from '@/components/layout/footer/Footer';


export default async function Home() {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <main className="flex-grow">
        <Hero />
        <Features />
        <InteractiveShowcase />
        <UserBenefits />
        <HowItWorks />
        <Testimonials />
        <CallToAction />
      </main>
      <Footer />
    </div>
  )
}

