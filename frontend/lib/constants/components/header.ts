import routes from '@/lib/constants/routes';

export const baseRoutes = [
  { path: routes.home, label: 'Home' },
  { path: routes.home + '#features', label: 'Features' },
  { path: routes.about, label: 'About' },
];

export const authRoutes = [
  { path: routes.home, label: 'Home' },
  { path: routes.explore, label: 'Explore' },
  { path: routes.blog, label: 'Blog' },
  { path: routes.aiSearch, label: 'AI Search' },
  { path: routes.map, label: 'Map' },
];
