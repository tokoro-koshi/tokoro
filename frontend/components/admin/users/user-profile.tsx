'use client';

import type React from 'react';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useMutation } from '@tanstack/react-query';
import { DateTime } from 'luxon';
import toast from 'react-hot-toast';
import { User } from '@/lib/types/user';
import { UserClient } from '@/lib/requests/user.client';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';

export default function UserProfile({ user }: { user: User }) {
  const router = useRouter();
  const [name, setName] = useState(user.name ?? '');

  // Update user name mutation
  const updateNameMutation = useMutation({
    mutationFn: async () => {
      await UserClient.updateUserName(user.userId, name);
    },
    onSuccess: () => {
      toast.success('Name updated successfully');
      router.refresh();
    },
    onError: () => {
      toast.error('Failed to update name');
    },
  });

  const blockMutation = useMutation({
    mutationFn: async () => {
      if (user.blocked) {
        UserClient.unblockUser(user.userId)
          .then(() => {
            toast.success('User unblocked successfully');
            router.refresh();
          })
          .catch(() => toast.error('Failed to unblock user'));
      } else {
        UserClient.blockUser(user.userId)
          .then(() => {
            toast.success('User blocked successfully');
            router.refresh();
          })
          .catch(() => toast.error('Failed to block user'));
      }
    },
  });

  const handleUpdateName = (e: React.FormEvent) => {
    e.preventDefault();
    updateNameMutation.mutate();
  };

  return (
    <div className='space-y-6'>
      <div className='flex items-center gap-4'>
        <UserProfile user={user} />
        <div>
          <h2 className='text-2xl font-bold'>{user.name}</h2>
          <p className='text-muted-foreground'>{user.email}</p>
          <div className='mt-2 flex gap-2'>
            <Badge variant={user.blocked ? 'destructive' : 'default'}>
              {user.blocked ? 'blocked' : 'active'}
            </Badge>
            {user.roles?.map((role) => (
              <Badge key={role} variant='outline'>
                {role}
              </Badge>
            )) || <Badge variant='outline'>user</Badge>}
          </div>
        </div>
        <div className='ml-auto'>
          <Button variant='outline' onClick={() => router.back()}>
            Back
          </Button>
        </div>
      </div>

      <Tabs defaultValue='profile' className='space-y-4'>
        <TabsList>
          <TabsTrigger value='profile'>Profile</TabsTrigger>
          <TabsTrigger value='security'>Security</TabsTrigger>
          <TabsTrigger value='activity'>Activity</TabsTrigger>
        </TabsList>

        <TabsContent value='profile' className='space-y-4'>
          <Card>
            <CardHeader>
              <CardTitle>Profile Information</CardTitle>
              <CardDescription>
                Update the user&#39;s profile information.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleUpdateName} className='space-y-4'>
                <div className='space-y-2'>
                  <Label htmlFor='name'>Name</Label>
                  <Input
                    id='name'
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                </div>
                <div className='space-y-2'>
                  <Label htmlFor='email'>Email</Label>
                  <Input id='email' value={user.email ?? ''} disabled />
                </div>
                <Button
                  type='submit'
                  disabled={updateNameMutation.isPending ?? name === user.name}
                >
                  {updateNameMutation.isPending
                    ? 'Updating...'
                    : 'Update Profile'}
                </Button>
              </form>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>User Preferences</CardTitle>
              <CardDescription>
                View and manage user preferences.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className='space-y-2'>
                <div className='flex justify-between'>
                  <span className='text-sm font-medium'>Theme</span>
                  <span className='text-sm'>
                    {user.userMetadata?.theme.toString() ?? 'Default'}
                  </span>
                </div>
                <div className='flex justify-between'>
                  <span className='text-sm font-medium'>Language</span>
                  <span className='text-sm'>
                    {user.userMetadata?.language.toString() ?? 'English'}
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value='security' className='space-y-4'>
          <Card>
            <CardHeader>
              <CardTitle>Security Settings</CardTitle>
              <CardDescription>
                Manage the user&#39;s security settings.
              </CardDescription>
            </CardHeader>
            <CardContent className='space-y-4'>
              <div className='space-y-2'>
                <Label>Account Status</Label>
                <div className='flex items-center justify-between'>
                  <span>
                    This account is currently{' '}
                    {user.blocked ? 'blocked' : 'active'}
                  </span>
                  <Button
                    variant={user.blocked ? 'default' : 'destructive'}
                    onClick={() => blockMutation.mutate()}
                    disabled={blockMutation.isPending}
                  >
                    {user.blocked ? 'Unblock Account' : 'Block Account'}
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value='activity' className='space-y-4'>
          <Card>
            <CardHeader>
              <CardTitle>Account Activity</CardTitle>
              <CardDescription>
                View the user&#39;s recent account activity.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className='space-y-4'>
                <div className='space-y-2'>
                  <div className='flex justify-between'>
                    <span className='text-sm font-medium'>Account Created</span>
                    <time suppressHydrationWarning className='text-sm'>
                      {user.createdAt
                        ? DateTime.fromISO(user.createdAt).toLocaleString()
                        : 'Unknown'}
                    </time>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
