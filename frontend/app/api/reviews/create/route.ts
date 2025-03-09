import { NextRequest, NextResponse } from 'next/server';
import { PlaceReviewClient } from '@/lib/requests/place-review.client';

export async function POST(request:NextRequest) :Promise<NextResponse>{
  const body = await request.json();
  const review = await PlaceReviewClient.savePlaceReview(body);
  return NextResponse.json(review);
}