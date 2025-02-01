import { ReactNode } from 'react';
import type { Metadata } from 'next';
import Provider from '@/app/providers';
import Header from '@/components/layout/header/header';
import Footer from '@/components/layout/footer/footer';
import '@/styles/globals.css';

export const metadata: Metadata = {
  title: 'Tokoro',
  description: '',
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: ReactNode;
}>) {
  return (
    <html lang='en'>
      <body className={`antialiased`}>
        <Provider>
          <div className='flex min-h-screen flex-col'>
            <Header />
            <main className='flex-grow'>{children}</main>
            <Footer />
          </div>
        </Provider>
      </body>
    </html>
  );
}
