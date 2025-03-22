import { NextRequest, NextResponse } from 'next/server';
import jwt, { JwtPayload } from 'jsonwebtoken';
import { getSession } from '@auth0/nextjs-auth0/edge';
import routes from '@/lib/constants/routes';

/*
 * This middleware is used to protect routes that require authentication.
 */
export const config = {
  matcher: ['/(admin|settings|explore|prompt)/:path*'],
};

export async function middleware(request: NextRequest) {
  try {
    const { pathname } = request.nextUrl;
    const response = new NextResponse();
    const session = await getSession(request, response);

    if (!session || !session.accessToken) {
      return NextResponse.redirect(new URL(routes.auth.login, request.url));
    }

    // Obtain roles
    const decoded = jwt.decode(session.accessToken) as JwtPayload;
    const roles = decoded['claims/roles'] as string[];

    // Check roles
    if (pathname.startsWith('/admin') && !roles.includes('ADMIN')) {
      return NextResponse.redirect(new URL('/404', request.url));
    }

    return NextResponse.next();
  } catch (error) {
    console.error('Middleware error:', error);
    return NextResponse.redirect(new URL(routes.home, request.url));
  }
}
