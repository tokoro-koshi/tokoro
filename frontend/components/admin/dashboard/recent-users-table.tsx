import { User } from '@/lib/types/user';
import UserAvatar from '@/components/user/avatar';
import { humanRelativeTime } from '@/lib/helpers/date';

export default function RecentUsersTable({ users }: { users: User[] }) {
  return (
    <div className='space-y-4'>
      {users.map((user) => (
        <div key={user.userId} className='flex items-center gap-4'>
          <UserAvatar user={user} />
          <div className='space-y-1'>
            <p className='text-sm font-medium leading-none'>{user.name}</p>
            <p className='text-sm text-muted-foreground'>{user.email}</p>
          </div>
          <div className='ml-auto text-sm text-muted-foreground'>
            {user.createdAt ? humanRelativeTime(user.createdAt) : 'Unknown'}
          </div>
        </div>
      ))}
    </div>
  );
}
