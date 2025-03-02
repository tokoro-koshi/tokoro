import { NextRequest, NextResponse } from 'next/server';
import { PlaceClient } from '@/lib/requests/place.client';

export async function POST(request:NextRequest) :Promise<NextResponse>{
  const body = await request.json();
  const places = await PlaceClient.searchPlaces(body.prompt);
  return NextResponse.json(places);
}