import { handleAuth, handleCallback, handleLogin, Session } from '@auth0/nextjs-auth0';
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
        if (session.accessToken) {
          const userData = await UserClient.getAuthenticatedUserByToken(session.accessToken);

          if (userData) {
            session.user = {
              ...session.user,
              ...userData,
            };
          }
        }
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
      return session;
    }
  }),
});

// export const me = async () => {
//   const session = await getSession();
//
//   const userData = await UserClient.getAuthenticatedUser();
//
//   if (!session) {
//     return NextResponse.json({ error: 'Not authenticated' }, { status: 401 });
//   }
//
//   if (userData) {
//     session.user = {
//       ...session.user,
//       ...userData,
//     };
//   }
//
//   return NextResponse.json({ user: session.user as User });
// };