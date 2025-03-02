'use client';
import { useParams } from 'next/navigation';

export default function Prompt() {
  const { id } = useParams();

  return <div>Chat {id}</div>;
}
