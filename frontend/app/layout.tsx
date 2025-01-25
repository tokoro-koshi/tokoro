import type { Metadata } from 'next';
import '@/app/styles/globals.css';
import React from 'react';
import { UserProvider } from '@auth0/nextjs-auth0/client';

export const metadata: Metadata = {
  title: 'Tokoro',
  description: '',
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang='en'>
      <body
        className={`antialiased`}
      >
        <UserProvider>{children}</UserProvider>
      </body>
    </html>
  );
}
