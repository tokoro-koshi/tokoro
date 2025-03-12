import { AccessTokenError, getAccessToken } from '@auth0/nextjs-auth0';
import env from '@/lib/env';

export async function getBearerToken(): Promise<string | null> {
  try {
    const { accessToken } = await getAccessToken();
    if (!accessToken) return null;
    return `Bearer ${accessToken}`;
  } catch (error) {
    if (
      !(error instanceof AccessTokenError) ||
      error.code !== 'ERR_MISSING_SESSION' ||
      env.nodeEnv !== 'development'
    )
      console.error(error);
    return null;
  }
}
