import { FavoritesCollection } from '@/lib/types/place';

export interface User {
  email: string;
  emailVerified: boolean;
  userId: string;
  picture: string;
  name: string;
  nickname: string;
  givenName: string;
  familyName: string;
  createdAt: string; // ISO date-time string
  updatedAt: string; // ISO date-time string
  identities: {
    connection: string;
    userId: string;
    provider: string;
    isSocial: boolean;
    accessToken: string;
    expiresIn: number;
  }[];
  lastIp: string;
  lastLogin: string; // ISO date-time string
  loginsCount: number;
  idpTenantDomain: string;
  connection?: string;
  clientId?: string;
  password?: string[];
  verifyPassword?: boolean;
  username?: string;
  verifyEmail?: boolean;
  phoneNumber?: string;
  phoneVerified?: boolean;
  verifyPhoneNumber?: boolean;
  appMetadata?: Record<string, object>;
  userMetadata?: {
    collections: FavoritesCollection[];
  } & Record<string, object>;
  multifactor?: string[];
  lastPasswordReset?: string; // ISO date-time string
  blocked?: boolean;
}
