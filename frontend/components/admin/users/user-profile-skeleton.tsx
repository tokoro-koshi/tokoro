import { Skeleton } from '@/components/ui/skeleton';
import { Card, CardContent, CardHeader } from '@/components/ui/card';

export default function UserProfileSkeleton() {
  return (
    <div className='space-y-6'>
      <div className='flex items-center gap-4'>
        <Skeleton className='h-20 w-20 rounded-full' />
        <div>
          <Skeleton className='mb-2 h-8 w-48' />
          <Skeleton className='mb-2 h-4 w-64' />
          <div className='mt-2 flex gap-2'>
            <Skeleton className='h-6 w-16' />
            <Skeleton className='h-6 w-16' />
          </div>
        </div>
        <div className='ml-auto'>
          <Skeleton className='h-10 w-20' />
        </div>
      </div>

      <Skeleton className='h-10 w-64' />

      <Card>
        <CardHeader>
          <Skeleton className='mb-2 h-6 w-48' />
          <Skeleton className='h-4 w-64' />
        </CardHeader>
        <CardContent className='space-y-4'>
          <div className='space-y-2'>
            <Skeleton className='h-4 w-16' />
            <Skeleton className='h-10 w-full' />
          </div>
          <div className='space-y-2'>
            <Skeleton className='h-4 w-16' />
            <Skeleton className='h-10 w-full' />
          </div>
          <Skeleton className='h-10 w-32' />
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <Skeleton className='mb-2 h-6 w-48' />
          <Skeleton className='h-4 w-64' />
        </CardHeader>
        <CardContent>
          <div className='space-y-2'>
            <div className='flex justify-between'>
              <Skeleton className='h-4 w-16' />
              <Skeleton className='h-4 w-16' />
            </div>
            <div className='flex justify-between'>
              <Skeleton className='h-4 w-16' />
              <Skeleton className='h-4 w-16' />
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
