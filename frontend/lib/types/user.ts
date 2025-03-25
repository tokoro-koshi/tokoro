import { FavoritesCollection } from '@/lib/types/place';

export interface User {
  username?: string;
  email: string;
  phoneNumber?: string;
  sid: string;
  userId: string;
  picture: string;
  name: string;
  nickname: string;
  givenName: string;
  familyName: string;
  createdAt: string; // ISO date-time string
  updatedAt: string; // ISO date-time string
  appMetadata?: Record<string, object>;
  userMetadata?: Record<string, object> & {
    collections: FavoritesCollection[];
  };
  blocked?: boolean;
  values?: Record<string, object>;
  roles: string[];
  permissions: string[];
}
