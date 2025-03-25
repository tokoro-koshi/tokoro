import { Skeleton } from '@/components/ui/skeleton';
import { Card, CardContent, CardHeader } from '@/components/ui/card';

export default function SettingsSkeleton() {
  return (
    <div className='space-y-4'>
      <Skeleton className='h-10 w-[300px]' />

      <Card>
        <CardHeader>
          <Skeleton className='mb-2 h-6 w-[150px]' />
          <Skeleton className='h-4 w-[250px]' />
        </CardHeader>
        <CardContent className='space-y-6'>
          <div className='space-y-4'>
            <div className='grid grid-cols-1 gap-4 md:grid-cols-2'>
              <div className='space-y-2'>
                <Skeleton className='h-4 w-[100px]' />
                <Skeleton className='h-10 w-full' />
              </div>
              <div className='space-y-2'>
                <Skeleton className='h-4 w-[100px]' />
                <Skeleton className='h-10 w-full' />
              </div>
            </div>

            <div className='grid grid-cols-1 gap-4 md:grid-cols-2'>
              <div className='space-y-2'>
                <Skeleton className='h-4 w-[100px]' />
                <Skeleton className='h-10 w-full' />
              </div>
              <div className='space-y-2'>
                <Skeleton className='h-4 w-[100px]' />
                <Skeleton className='h-10 w-full' />
              </div>
            </div>
          </div>

          <Skeleton className='h-px w-full' />

          <Skeleton className='h-10 w-[120px]' />
        </CardContent>
      </Card>
    </div>
  );
}
