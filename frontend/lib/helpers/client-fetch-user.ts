import axios from 'axios';
import { User } from '@/lib/types/user';

export const getUser = async (
  user: User | null,
  setUser: (user: User | null) => void
): Promise<void> => {
  const fetchedUser = await axios.get('/api/user/details');
  if (!fetchedUser.data || !user) {
    console.error('Failed to fetch user data');
    return;
  }
  setUser({
    ...user,
    userMetadata: fetchedUser.data.userMetadata,
  });
  return;
};
