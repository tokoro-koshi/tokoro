import { PlaceClient } from '@/lib/requests/place.client';
import BlogComponent from '@/components/blog/blog';

// Remove caching because of cookies
export const dynamic = 'force-dynamic';

export default async function BlogPage() {
  const places = await PlaceClient.getRandomPlaces(5);
  return <BlogComponent places={places} />;
}
