import { AccessTokenError, getAccessToken } from '@auth0/nextjs-auth0';
import env from '@/lib/env';

export async function getBearerToken(): Promise<string | null> {
  try {
    const {accessToken} = await getAccessToken();
    if (!accessToken) return null;
    return `Bearer ${accessToken}`;
  } catch (e) {
    if (
      !(e instanceof AccessTokenError) || 
      e.code !== 'ERR_MISSING_SESSION' ||
      env.nodeEnv !== 'development'
    )
      console.error(e);
    return null;
  }
}
