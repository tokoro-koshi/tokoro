import { ReactNode } from 'react';
import type { Metadata } from 'next';
import { UserProvider } from '@auth0/nextjs-auth0/client';
import Header from '@/components/layout/header/Header';
import Footer from '@/components/layout/footer/Footer';
import '@/app/styles/globals.css';

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
        <UserProvider>
          <div className='flex min-h-screen flex-col'>
            <Header />
            <main className='flex-grow'>{children}</main>
            <Footer />
          </div>
        </UserProvider>
      </body>
    </html>
  );
}
