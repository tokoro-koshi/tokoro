import { NextRequest, NextResponse } from 'next/server';
import { notFound } from 'next/navigation';
import { decodeJwt } from 'jose';
import { getSession, updateSession } from '@auth0/nextjs-auth0/edge';
import routes from '@/lib/constants/routes';

/*
 * This middleware is used to protect routes that require authentication.
 */
export const config = {
  matcher: ['/(admin|settings|explore|prompt|api/admin|blog)/:path*'],
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
    const jwtPayload = decodeJwt(session.accessToken);
    const roles = jwtPayload['claims/roles'] as string[];
    await updateSession(request, response, { ...session, roles });

    // Check roles for admin routes (/admin/**, /api/admin/**)
    if (/^\/(api\/)?(admin|blog)/.test(pathname) && !roles.includes('ADMIN')) {
      notFound();
    }

    return NextResponse.next();
  } catch (error) {
    console.error('Middleware error:', error);
    return NextResponse.redirect(new URL(routes.home, request.url));
  }
}
