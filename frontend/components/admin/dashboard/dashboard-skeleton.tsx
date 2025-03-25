import { Skeleton } from '@/components/ui/skeleton';
import { Card, CardContent, CardHeader } from '@/components/ui/card';

export default function DashboardSkeleton() {
  return (
    <div className='space-y-6'>
      <div className='grid gap-4 md:grid-cols-2 lg:grid-cols-4'>
        {Array(4)
          .fill(0)
          .map((_, i) => (
            <Card key={i}>
              <CardHeader className='flex flex-row items-center justify-between space-y-0 pb-2'>
                <Skeleton className='h-4 w-[100px]' />
              </CardHeader>
              <CardContent>
                <Skeleton className='mb-2 h-8 w-[60px]' />
                <Skeleton className='h-4 w-[120px]' />
              </CardContent>
            </Card>
          ))}
      </div>

      <div className='space-y-4'>
        <Skeleton className='h-10 w-[300px]' />

        <div className='grid gap-4 md:grid-cols-2 lg:grid-cols-7'>
          <Card className='col-span-4'>
            <CardHeader>
              <Skeleton className='h-6 w-[150px]' />
            </CardHeader>
            <CardContent>
              <Skeleton className='h-[200px] w-full' />
            </CardContent>
          </Card>

          <Card className='col-span-3'>
            <CardHeader>
              <Skeleton className='mb-2 h-6 w-[120px]' />
              <Skeleton className='h-4 w-[180px]' />
            </CardHeader>
            <CardContent>
              <div className='space-y-2'>
                {Array(5)
                  .fill(0)
                  .map((_, i) => (
                    <Skeleton key={i} className='h-12 w-full' />
                  ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
