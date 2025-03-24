import { UserProfile } from '@auth0/nextjs-auth0/client';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

export default function UserAvatar({ user }: { user?: UserProfile | null }) {
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
