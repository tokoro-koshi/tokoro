'use client';
import { useParams } from 'next/navigation';

export default function Post() {
  const { id } = useParams();
  return <div>Post {id}</div>;
}
