import {
  PersonStandingIcon as PersonIcon,
  BookmarkIcon,
  SearchIcon,
} from 'lucide-react';

export const features = [
  {
    title: 'Personalized Recommendations',
    description:
      'Get tailored suggestions based on your preferences and past visits.',
    icon: <PersonIcon className='h-6 w-6' />,
  },
  {
    title: 'Saved Places and History',
    description:
      'Keep track of your favorite spots and revisit your journey anytime.',
    icon: <BookmarkIcon className='h-6 w-6' />,
  },
  {
    title: 'AI-driven Quick Search',
    description:
      "Find exactly what you're looking for with our intelligent search feature.",
    icon: <SearchIcon className='h-6 w-6' />,
  },
];
