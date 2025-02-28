import React, { ReactNode } from 'react';
import type { Metadata } from 'next';
import { cn } from '@/lib/utils';
import { AllFontsVariables } from '@/fonts';
import Provider from '@/app/providers';
import Footer from '@/components/layout/footer/footer';
import '@/styles/globals.css';
import Header from '@/components/layout/header/header';

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
          <main className={'wavy-background min-h-svh pt-24'}>
            <Header />
            {children}
          </main>
          <Footer />
        </Provider>
      </body>
    </html>
  );
}
