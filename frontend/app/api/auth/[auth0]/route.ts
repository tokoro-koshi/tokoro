import { handleAuth, handleLogin } from '@auth0/nextjs-auth0';

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
});
