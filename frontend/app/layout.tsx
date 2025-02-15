import { ReactNode } from 'react';
import type { Metadata } from 'next';
import { cn } from '@/lib/utils';
import { JetBrainsMono, Raleway } from '@/fonts';
import Provider from '@/app/providers';
import Footer from '@/components/layout/footer/footer';
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
      <body className={cn(Raleway.variable, JetBrainsMono.variable)}>
        <Provider>
          <div className='flex min-h-screen flex-col'>
            {children}
            <Footer />
          </div>
        </Provider>
      </body>
    </html>
  );
}
