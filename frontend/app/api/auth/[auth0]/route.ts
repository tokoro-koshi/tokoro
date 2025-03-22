import {
  handleAuth,
  handleCallback,
  handleLogin,
  Session,
} from '@auth0/nextjs-auth0';
import { NextRequest } from 'next/server';
import { UserClient } from '@/lib/requests/user.client';

/**
 * /api/auth/[auth0], where [auth0] - login, logout, callback, me
 */
export const GET = handleAuth({
  login: handleLogin({
    authorizationParams: {
      audience: process.env.AUTH0_AUDIENCE,
      scope: 'openid profile email',
    },
  }),
  signup: handleLogin({
    authorizationParams: {
      audience: process.env.AUTH0_AUDIENCE,
      scope: 'openid profile email',
      screen_hint: 'signup',
    },
  }),
  callback: handleCallback({
    async afterCallback(_req: NextRequest, session: Session) {
      try {
        if (!session.accessToken) return;

        const userData = await UserClient.getAuthenticatedUserByToken(
          session.accessToken
        );

        if (!userData) return;

        session.user = {
          ...session.user,
          ...userData,
        };
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
      return session;
    },
  }),
});
