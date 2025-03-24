import React, { ReactNode } from 'react';
import type { Metadata } from 'next';
import { cn } from '@/lib/utils';
import { AllFontsVariables } from '@/fonts';
import Provider from '@/app/providers';
import Footer from '@/components/layout/footer/footer';
import Header from '@/components/layout/header/header';
import { Toaster } from 'react-hot-toast';
import '@/styles/globals.css';

export const metadata: Metadata = {
  title: 'Tokoro',
  description: '',
  icons: {
    icon: `/logo.svg`,
  },
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: ReactNode;
}>) {
  return (
    <html lang='en'>
      <body className={cn(AllFontsVariables)}>
        <Provider>
          <main className='wavy-background min-h-svh'>
            <Header />
            {children}
          </main>
          <Footer />
        </Provider>
        <Toaster position='top-right' />
      </body>
    </html>
  );
}
