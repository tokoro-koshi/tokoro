import {
  Raleway as RalewayConstructor,
  JetBrains_Mono as JetBrainsMonoConstructor,
} from 'next/font/google';

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

export const AllFonts = [Raleway, JetBrainsMono];
export const AllFontsVariables = AllFonts.map((f) => f.variable).join(' ');
