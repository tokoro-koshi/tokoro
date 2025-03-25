'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useQuery, useMutation } from '@tanstack/react-query';
import {
  MoreHorizontal,
  Search,
  UserPlus,
  Shield,
  Ban,
  Trash2,
  CheckCircle,
} from 'lucide-react';
import toast from 'react-hot-toast';
import { UserClient } from '@/lib/requests/user.client';
import { User } from '@/lib/types/user';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';
import UserAvatar from '@/components/user/avatar';
import styles from '@/components/admin/admin.module.css';

export default function UsersTable({ initialUsers }: { initialUsers: User[] }) {
  const router = useRouter();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

  // Use React Query to fetch users
  const { data: users = initialUsers } = useQuery({
    queryKey: ['users', searchQuery],
    queryFn: async () => {
      if (!searchQuery) return initialUsers;

      try {
        // In a real implementation, you would call your API with the search query
        // For now, we'll just filter the initial users
        return initialUsers.filter(
          (user) =>
            user.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
            user.email?.toLowerCase().includes(searchQuery.toLowerCase())
        );
      } catch (error) {
        toast.error('Failed to search users');
        console.error(error);
        return initialUsers;
      }
    },
    initialData: initialUsers,
  });

  // Block user mutation
  const blockUserMutation = useMutation({
    mutationFn: async (userId: string) => {
      await UserClient.blockUser(userId);
    },
    onSuccess: () => {
      toast.success('User blocked successfully');
      // Refresh the data
      router.refresh();
    },
    onError: () => {
      toast.error('Failed to block user');
    },
  });

  // Unblock user mutation
  const unblockUserMutation = useMutation({
    mutationFn: async (userId: string) => {
      await UserClient.unblockUser(userId);
    },
    onSuccess: () => {
      toast.success('User unblocked successfully');
      // Refresh the data
      router.refresh();
    },
    onError: () => {
      toast.error('Failed to unblock user');
    },
  });

  // Delete user mutation
  const deleteUserMutation = useMutation({
    mutationFn: async (userId: string) => {
      await UserClient.deleteUser(userId);
    },
    onSuccess: () => {
      setIsDeleteDialogOpen(false);
      toast.success('User deleted successfully');
      // Refresh the data
      router.refresh();
    },
    onError: () => {
      toast.error('Failed to delete user');
      setIsDeleteDialogOpen(false);
    },
  });

  const handleBlockUser = (user: User) => {
    if (!user?.userId) return;
    blockUserMutation.mutate(user.userId);
  };

  const handleUnblockUser = (user: User) => {
    if (!user?.userId) return;
    unblockUserMutation.mutate(user.userId);
  };

  const handleDeleteUser = () => {
    if (!selectedUser?.userId) return;
    deleteUserMutation.mutate(selectedUser.userId);
  };

  const handleManageRoles = (user: User) => {
    router.push(`/admin/users/${user.userId}/roles`);
  };

  const handleViewDetails = (user: User) => {
    router.push(`/admin/users/${user.userId}`);
  };

  return (
    <div className='space-y-4'>
      <div className={styles.actionBar}>
        <div className={styles.searchContainer}>
          <Search className={styles.searchIcon} />
          <Input
            type='search'
            placeholder='Search users...'
            className={styles.searchInput}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
        <Button>
          <UserPlus className='mr-2 h-4 w-4' />
          Add User
        </Button>
      </div>

      <div className={styles.tableContainer}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>User</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Roles</TableHead>
              <TableHead>Created</TableHead>
              <TableHead className='text-right'>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.userId}>
                <TableCell>
                  <div className='flex items-center gap-3'>
                    <UserAvatar user={user} />
                    <div>
                      <div className='font-medium'>{user.name}</div>
                      <div className='text-sm text-muted-foreground'>
                        {user.email}
                      </div>
                    </div>
                  </div>
                </TableCell>
                <TableCell>
                  <Badge variant={user.blocked ? 'destructive' : 'default'}>
                    {user.blocked ? 'blocked' : 'active'}
                  </Badge>
                </TableCell>
                <TableCell>
                  <div className='flex gap-1'>
                    {
                      /*user.roles?.map((role) => (
                      <Badge key={role} variant="outline">
                        {role}
                      </Badge>
                    )) ||*/ <Badge variant='outline'>user</Badge>
                    }
                  </div>
                </TableCell>
                <TableCell>
                  {/*{user.createdAt ? humanRelativeTime(user.createdAt) : "Unknown"}*/}
                </TableCell>
                <TableCell className='text-right'>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant='ghost' size='icon'>
                        <MoreHorizontal className='h-4 w-4' />
                        <span className='sr-only'>Open menu</span>
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align='end'>
                      <DropdownMenuLabel>Actions</DropdownMenuLabel>
                      <DropdownMenuItem onClick={() => handleViewDetails(user)}>
                        View details
                      </DropdownMenuItem>
                      <DropdownMenuItem onClick={() => handleManageRoles(user)}>
                        <Shield className='mr-2 h-4 w-4' />
                        Manage roles
                      </DropdownMenuItem>
                      <DropdownMenuSeparator />
                      {!user.blocked ? (
                        <DropdownMenuItem onClick={() => handleBlockUser(user)}>
                          <Ban className='mr-2 h-4 w-4' />
                          Block user
                        </DropdownMenuItem>
                      ) : (
                        <DropdownMenuItem
                          onClick={() => handleUnblockUser(user)}
                        >
                          <CheckCircle className='mr-2 h-4 w-4' />
                          Unblock user
                        </DropdownMenuItem>
                      )}
                      <DropdownMenuItem
                        className='text-destructive focus:text-destructive'
                        onClick={() => {
                          setSelectedUser(user);
                          setIsDeleteDialogOpen(true);
                        }}
                      >
                        <Trash2 className='mr-2 h-4 w-4' />
                        Delete user
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <Dialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Delete User</DialogTitle>
            <DialogDescription>
              Are you sure you want to delete this user? This action cannot be
              undone.
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              variant='outline'
              onClick={() => setIsDeleteDialogOpen(false)}
            >
              Cancel
            </Button>
            <Button
              variant='destructive'
              onClick={handleDeleteUser}
              disabled={deleteUserMutation.isPending}
            >
              {deleteUserMutation.isPending ? 'Deleting...' : 'Delete'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
