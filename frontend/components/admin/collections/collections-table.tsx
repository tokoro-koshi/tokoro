'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useQuery, useMutation } from '@tanstack/react-query';
import {
  MoreHorizontal,
  Search,
  Plus,
  Eye,
  Edit,
  Trash2,
  Globe,
  Lock,
} from 'lucide-react';
import toast from 'react-hot-toast';
import { FavoritesClient } from '@/lib/requests/favorites.client';
import type { FavoritesCollection } from '@/lib/types/place';
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
import styles from '@/components/admin/admin.module.css';
import { humanRelativeTime } from '@/lib/helpers/date';

interface CollectionWithDetails extends FavoritesCollection {
  userName: string;
  placesCount: number;
  isPublic: boolean;
}

export default function CollectionsTable({
  initialCollections,
}: {
  initialCollections: CollectionWithDetails[];
}) {
  const router = useRouter();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCollection, setSelectedCollection] =
    useState<CollectionWithDetails | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

  // Use React Query to fetch collections
  const { data: collections = initialCollections } = useQuery({
    queryKey: ['collections', searchQuery],
    queryFn: async () => {
      if (!searchQuery) return initialCollections;

      try {
        const searchResults =
          await FavoritesClient.searchCollectionsByName(searchQuery);
        // Map the search results to include the additional properties
        return searchResults.map((collection) => {
          const existingCollection = initialCollections.find(
            (c) => c.id === collection.id
          );
          return {
            ...collection,
            userName: existingCollection?.userName || 'Unknown',
            placesCount: collection.placesIds.length,
            isPublic: existingCollection?.isPublic || false,
          };
        });
      } catch (error) {
        toast.error('Failed to search collections');
        console.error(error);
        return initialCollections;
      }
    },
    initialData: initialCollections,
  });

  // Delete collection mutation
  const deleteCollectionMutation = useMutation({
    mutationFn: async (collectionId: string) => {
      await FavoritesClient.deleteCollection(collectionId);
    },
    onSuccess: () => {
      setIsDeleteDialogOpen(false);
      toast.success('Collection deleted successfully');
      router.refresh();
    },
    onError: () => {
      toast.error('Failed to delete collection');
      setIsDeleteDialogOpen(false);
    },
  });

  const handleDeleteCollection = () => {
    if (selectedCollection) {
      deleteCollectionMutation.mutate(selectedCollection.id);
    }
  };

  const handleViewCollection = (collection: CollectionWithDetails) => {
    router.push(`/admin/collections/${collection.id}`);
  };

  const handleEditCollection = (collection: CollectionWithDetails) => {
    router.push(`/admin/collections/${collection.id}/edit`);
  };

  const handleViewUser = (userId: string) => {
    router.push(`/admin/users/${userId}`);
  };

  return (
    <div className='space-y-4'>
      <div className={styles.actionBar}>
        <div className={styles.searchContainer}>
          <Search className={styles.searchIcon} />
          <Input
            type='search'
            placeholder='Search collections...'
            className={styles.searchInput}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
        <Button>
          <Plus className='mr-2 h-4 w-4' />
          Add Collection
        </Button>
      </div>

      <div className={styles.tableContainer}>
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
            {collections.map((collection) => (
              <TableRow key={collection.id}>
                <TableCell>
                  <div>
                    <div className='font-medium'>{collection.name}</div>
                  </div>
                </TableCell>
                <TableCell>
                  <Button
                    variant='link'
                    className='h-auto p-0 font-normal'
                    onClick={() => handleViewUser(collection.userId)}
                  >
                    {collection.userName}
                  </Button>
                </TableCell>
                <TableCell>{collection.placesCount}</TableCell>
                <TableCell>
                  <Badge variant={collection.isPublic ? 'default' : 'outline'}>
                    {collection.isPublic ? (
                      <Globe className='mr-1 h-3 w-3' />
                    ) : (
                      <Lock className='mr-1 h-3 w-3' />
                    )}
                    {collection.isPublic ? 'Public' : 'Private'}
                  </Badge>
                </TableCell>
                <TableCell>
                  {collection.createdAt
                    ? humanRelativeTime(collection.createdAt)
                    : 'Unknown'}
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
                      <DropdownMenuItem
                        onClick={() => handleViewCollection(collection)}
                      >
                        <Eye className='mr-2 h-4 w-4' />
                        View details
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => handleEditCollection(collection)}
                      >
                        <Edit className='mr-2 h-4 w-4' />
                        Edit collection
                      </DropdownMenuItem>
                      <DropdownMenuSeparator />
                      <DropdownMenuItem
                        className='text-destructive focus:text-destructive'
                        onClick={() => {
                          setSelectedCollection(collection);
                          setIsDeleteDialogOpen(true);
                        }}
                      >
                        <Trash2 className='mr-2 h-4 w-4' />
                        Delete collection
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
            <DialogTitle>Delete Collection</DialogTitle>
            <DialogDescription>
              Are you sure you want to delete this collection? This action
              cannot be undone.
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
              onClick={handleDeleteCollection}
              disabled={deleteCollectionMutation.isPending}
            >
              {deleteCollectionMutation.isPending ? 'Deleting...' : 'Delete'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
