import { handleAuth, handleLogin } from '@auth0/nextjs-auth0';

/**
 * /api/auth/[auth0], where [auth0] - login, logout, callback, me
 */
export const GET = handleAuth({
  signup: handleLogin({ authorizationParams: { screen_hint: 'signup' } })
});
