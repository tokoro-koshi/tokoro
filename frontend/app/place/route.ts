import { redirect } from 'next/navigation';
import routes from '@/lib/constants/routes';

export async function GET() {
  redirect(routes.explore);
}
