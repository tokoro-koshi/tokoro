import { NextRequest, NextResponse } from 'next/server';
import { PlaceReviewClient } from '@/lib/requests/place-review.client';

export async function PUT(request: NextRequest): Promise<NextResponse> {
  const searchParams = request.nextUrl.searchParams;
  const id = searchParams.get('id');
  if (!id) {
    return NextResponse.json(
      { error: 'Missing id parameter' },
      { status: 400 }
    );
  }

  const body = await request.json();

  try {
    const review = await PlaceReviewClient.updatePlaceReview(id, body);
    return NextResponse.json(review);
  } catch (error) {
    console.error(error);
    return NextResponse.json(
      { error: 'Failed to update review' },
      { status: 500 }
    );
  }
}
