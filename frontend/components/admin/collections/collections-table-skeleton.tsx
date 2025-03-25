import { Skeleton } from '@/components/ui/skeleton';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

export default function CollectionsTableSkeleton() {
  return (
    <div className='space-y-4'>
      <div className='flex justify-between'>
        <Skeleton className='h-10 w-64' />
        <Skeleton className='h-10 w-32' />
      </div>

      <div className='rounded-md border'>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Collection</TableHead>
              <TableHead>Owner</TableHead>
              <TableHead>Places</TableHead>
              <TableHead>Visibility</TableHead>
              <TableHead>Created</TableHead>
              <TableHead className='text-right'>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {Array(5)
              .fill(0)
              .map((_, i) => (
                <TableRow key={i}>
                  <TableCell>
                    <div>
                      <Skeleton className='mb-2 h-4 w-32' />
                      <Skeleton className='h-3 w-40' />
                    </div>
                  </TableCell>
                  <TableCell>
                    <Skeleton className='h-4 w-24' />
                  </TableCell>
                  <TableCell>
                    <Skeleton className='h-4 w-8' />
                  </TableCell>
                  <TableCell>
                    <Skeleton className='h-6 w-16' />
                  </TableCell>
                  <TableCell>
                    <Skeleton className='h-4 w-24' />
                  </TableCell>
                  <TableCell className='text-right'>
                    <Skeleton className='ml-auto h-8 w-8' />
                  </TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}
