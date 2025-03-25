import { UserProfile } from '@auth0/nextjs-auth0/client';
import { User } from '@/lib/types/user';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

export default function UserAvatar({
  user,
}: {
  user?: UserProfile | User | null;
}) {
  if (!user) return null;
  return (
    <Avatar>
      <AvatarImage src={user.picture ?? ''} />
      <AvatarFallback className={'text-white'}>
        {user.name
          ?.split(' ')
          .map((x) => x.charAt(0))
          .join('') ?? '?'}
      </AvatarFallback>
    </Avatar>
  );
}
