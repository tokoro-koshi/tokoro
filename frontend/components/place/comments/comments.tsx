'use client';

import type React from 'react';

import { useState, useRef } from 'react';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { CalendarDays, PencilLine, Trash } from 'lucide-react';
import { useUser } from '@/lib/stores/user';
import { PlaceReview } from '@/lib/types/place-review';
import styles from './comments.module.css';
import axios from 'axios';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { DateTime } from 'luxon';
import { useMutation } from '@tanstack/react-query';
import toast from "react-hot-toast";

interface CommentSectionProps {
  placeId: string;
  initialComments?: PlaceReview[];
}

export function Comments({
  placeId,
  initialComments = [],
}: CommentSectionProps) {
  const [comments, setComments] = useState<PlaceReview[]>(
    initialComments?.sort(
      (a, b) =>
        DateTime.fromISO(b.createdAt || '').toMillis() -
        DateTime.fromISO(a.createdAt || '').toMillis()
    ) || []
  );
  const [newComment, setNewComment] = useState('');
  const commentListRef = useRef<HTMLDivElement>(null);

  const { user, isLoading: isUserLoading } = useUser();

  const { mutate: addComment, isPending: isSubmittingComment } = useMutation({
    mutationKey: ['createComment', placeId],
    mutationFn: async (comment: string) => {
      if (!comment.trim() || !user?.userId) return null;

      const newCommentData: PlaceReview = {
        userId: user.userId,
        placeId,
        comment,
        isRecommended: false,
      };

      const { data } = await axios.post('/api/reviews/create', newCommentData);
      return data;
    },
    onSuccess: (newCommentData) => {
      if (!newCommentData) return;

      newCommentData.userName = user?.name ?? 'You';
      newCommentData.userAvatar = user?.picture ?? '';

      setComments((prev) => [newCommentData, ...prev]);
      setNewComment('');
    },
    onError: (error) => {
      toast.error('Failed to add comment')
      console.error('Failed to add comment:', error);
    },
  })

  const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setNewComment(e.target.value);
    e.target.style.height = 'auto';
    e.target.style.height = `${e.target.scrollHeight}px`;
  };
  return (
    !isUserLoading && (
      <div className={styles.container}>
        {user && (
          <div className={styles.commentInputSection}>
            <Avatar className={styles.avatar}>
              <AvatarImage src={user.picture ?? ''} />
              <AvatarFallback>{user?.name?.substring(0, 3)}</AvatarFallback>
            </Avatar>
            <Textarea
              value={newComment}
              onChange={handleTextareaChange}
              placeholder='Add your comment'
              className={styles.textarea}
              rows={1}
            />
            <Button
              size='icon'
              className={styles.button}
              onClick={() => addComment(newComment)}
              disabled={isUserLoading || isSubmittingComment || !newComment.trim()}
            >
              <span>Send</span>
            </Button>
          </div>
        )}

        <div className={styles.commentsList} ref={commentListRef}>
          {!isUserLoading && comments.length > 0 ? (
            comments.map((comment) => (
              <CommentItem
                key={comment.id}
                comment={comment}
                userId={user?.userId || ''}
              />
            ))
          ) : (
            <div className='py-14 text-center text-3xl font-bold text-muted-foreground'>
              No comments yet, be the first one to post
            </div>
          )}
        </div>
      </div>
    )
  );
}

export default function CommentItem({
  comment,
  userId,
}: {
  comment: PlaceReview;
  userId?: string;
}) {
  const [editedComment, setEditedComment] = useState<string>(comment.comment);
  const [commentDisplay, setCommentDisplay] = useState<string>(comment.comment);
  const [isDeleted, setIsDeleted] = useState(false);

  const handleSumbitEdit = async () => {
    if (editedComment === comment.comment || editedComment === '') return;

    try {
      await axios.put(`/api/reviews/edit?id=${comment.id}`, {
        placeId: comment.placeId,
        userId: comment.userId,
        comment: editedComment,
        isRecommended: comment.isRecommended,
      });

      comment.comment = editedComment;
      setCommentDisplay(editedComment);
    } catch (error) {
      console.error('Failed to edit comment:', error);
    }
  };

  const handleSubmitDelete = async () => {
    await axios.delete(`/api/reviews/delete?id=${comment.id}`);
    setIsDeleted(true);
  };
  if (isDeleted) return null;

  return (
    <div className={styles.commentItem}>
      <Avatar className={styles.commentItemAvatar}>
        <AvatarImage
          src={comment.userAvatar || comment.userId}
          alt={comment.userName || 'User'}
        />
        <AvatarFallback>
          {(comment.userName || 'U').substring(0, 3)}
        </AvatarFallback>
      </Avatar>
      <div className={styles.commentItemContent}>
        <div className={styles.commentItemHeader}>
          <span>{comment.userName || 'Anonymous'}</span>
        </div>
        <p className={styles.commentItemText}>{commentDisplay}</p>
        <div className='flex'>
          <span
            className={styles.commentItemFooter}
            title={
              'Posted on ' +
              DateTime.fromISO(comment.createdAt || '').toFormat('dd.MM.yyyy')
            }
          >
            <CalendarDays className={styles.icon} />
            {DateTime.fromISO(comment.createdAt || '').toRelative({
              locale: 'en',
            })}
          </span>
          {comment.createdAt !== comment.updatedAt && (
            <>
              /{' '}
              <span
                title={
                  'Edited on ' +
                  DateTime.fromISO(comment.updatedAt || '').toFormat(
                    'dd.MM.yyyy'
                  )
                }
                className={styles.commentItemFooter}
              >
                <PencilLine className={styles.icon} />
                {DateTime.fromISO(comment.updatedAt || '').toRelative({
                  locale: 'en',
                })}
              </span>
            </>
          )}
        </div>
        {comment.userId === userId && (
          <div className='absolute right-4 top-4 space-x-2'>
            <Dialog>
              <DialogTrigger asChild>
                <Button size='icon' className={styles.icon}>
                  <PencilLine />
                </Button>
              </DialogTrigger>
              <DialogContent className='sm:max-w-[425px]'>
                <DialogHeader>
                  <DialogTitle>Edit Comment</DialogTitle>
                </DialogHeader>
                <div className='grid gap-4 py-4'>
                  <div className='items-center gap-4'>
                    <Textarea
                      onChange={(e) => setEditedComment(e.target.value)}
                      defaultValue={editedComment}
                      placeholder='Your comment'
                      id='name'
                      className='bg-foreground text-background placeholder:text-background placeholder:opacity-50'
                    />
                  </div>
                </div>
                <DialogFooter>
                  <DialogClose asChild>
                    <Button type='submit'>Cancel</Button>
                  </DialogClose>
                  <DialogClose asChild>
                    <Button
                      className={styles.editButton}
                      type='submit'
                      onClick={handleSumbitEdit}
                    >
                      Save changes
                    </Button>
                  </DialogClose>
                </DialogFooter>
              </DialogContent>
            </Dialog>
            <Dialog>
              <DialogTrigger asChild>
                <Button size='icon' className={styles.icon}>
                  <Trash />
                </Button>
              </DialogTrigger>
              <DialogContent className='sm:max-w-[425px]'>
                <DialogHeader>
                  <DialogTitle>
                    Are you sure you want to delete that comment?
                  </DialogTitle>
                </DialogHeader>
                <DialogFooter>
                  <DialogClose asChild>
                    <Button type='submit'>Cancel</Button>
                  </DialogClose>
                  <DialogClose asChild>
                    <Button
                      className={styles.deleteButton}
                      type='submit'
                      onClick={handleSubmitDelete}
                    >
                      Delete
                    </Button>
                  </DialogClose>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>
        )}
      </div>
    </div>
  );
}
