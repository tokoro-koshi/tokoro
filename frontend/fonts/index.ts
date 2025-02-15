import { Raleway as RalewayConstructor } from 'next/font/google';
import { JetBrains_Mono as JetBrainsMonoConstructor } from 'next/font/google';

export const Raleway = RalewayConstructor({
  subsets: ['latin', 'latin-ext', 'cyrillic', 'cyrillic-ext'],
  display: 'swap',
  style: ['normal', 'italic'],
  variable: '--font-raleway',
});

export const JetBrainsMono = JetBrainsMonoConstructor({
  subsets: ['latin', 'latin-ext', 'cyrillic', 'cyrillic-ext'],
  display: 'swap',
  style: ['normal', 'italic'],
  variable: '--font-jetbrains-mono',
});
