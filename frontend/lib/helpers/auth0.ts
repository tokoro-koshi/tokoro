import { AccessTokenError, getAccessToken } from '@auth0/nextjs-auth0';

export async function getBearerToken(): Promise<string | null> {
  try {
    const token = (await getAccessToken()).accessToken;
    if (!token) return null;
    return `Bearer ${token}`;
  } catch (e) {
    if (!(e instanceof AccessTokenError) || e.code !== 'ERR_MISSING_SESSION')
      console.error(e);

    return null;
  }
}
