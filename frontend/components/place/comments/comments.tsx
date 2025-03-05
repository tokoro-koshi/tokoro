"use client"

import type React from "react"

import { useState, useRef, useEffect } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"
import { CalendarDays } from 'lucide-react';
import { format, formatDistanceToNow } from 'date-fns';
import { useUser } from '@/lib/stores/user';
import { PlaceReview } from '@/lib/types/place-review';
import styles from './comments.module.css';

interface CommentSectionProps {
  initialComments?: PlaceReview[]
}

export function Comments({
                           initialComments = [],
                         }: CommentSectionProps) {
  
  // const [comments, setComments] = useState<PlaceReview[]>(initialComments)
  const comments = initialComments
  const [newComment, setNewComment] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [isLoadingMore, setIsLoadingMore] = useState(false)
  const commentListRef = useRef<HTMLDivElement>(null)
  const observerRef = useRef<IntersectionObserver | null>(null)
  const loadingRef = useRef<HTMLDivElement>(null)

  const { user } = useUser();

  // Handle submitting a new comment
  const handleSubmitComment = async () => {
    if (!newComment.trim()) return

    setIsLoading(true)
    try {
      // const commentData = {
      //   userId: user?.sub,
      //   placeId,
      //   comment: newComment,
      //   isRecommended: true,
      // }

      // if (onAddComment) {
      //   await onAddComment(commentData)
      // }

      // Optimistically add the comment to the list
      // const now = new Date().toISOString()
      // setComments((prev) => [
      //   {
      //     id: `temp-${Date.now()}`,
      //     ...commentData,
      //     createdAt: now,
      //     updatedAt: now,
      //     userName: currentUser.name,
      //     userAvatar: currentUser.avatar,
      //   },
      //   ...prev,
      // ])

      setNewComment("")
    } catch (error) {
      console.error("Failed to add comment:", error)
    } finally {
      setIsLoading(false)
    }
  }

// const handleAddComment = async (commentData: any) => {
//   // In a real app, this would send data to your API
//   console.log("Adding comment:", commentData)
//   return Promise.resolve()
// }
//
//   const handleLoadMoreComments = async (lastCommentId: string) => {
//     // In a real app, this would fetch more comments from your API
//     console.log("Loading more comments after:", lastCommentId)
//     return Promise.resolve([])
//   }

  // Set up intersection observer for infinite scrolling
  useEffect(() => {
    // if (!onLoadMoreComments) return

    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries
        if (entry.isIntersecting && !isLoadingMore && comments.length >= 3) {
          loadMoreComments()
        }
      },
      { threshold: 0.1 },
    )

    observerRef.current = observer

    if (loadingRef.current) {
      observer.observe(loadingRef.current)
    }

    return () => {
      if (observerRef.current) {
        observerRef.current.disconnect()
      }
    }
  }, [comments, isLoadingMore,
    // onLoadMoreComments
  ])

  // Load more comments when scrolling to bottom
  const loadMoreComments = async () => {
    // if (!onLoadMoreComments || comments.length === 0) return

    setIsLoadingMore(true)
    try {
      // const lastCommentId = comments[comments.length - 1].id
      // const moreComments = await onLoadMoreComments(lastCommentId)
      //
      // if (moreComments.length > 0) {
      //   setComments((prev) => [...prev, ...moreComments])
      // }
    } catch (error) {
      console.error("Failed to load more comments:", error)
    } finally {
      setIsLoadingMore(false)
    }
  }

  // Auto-resize textarea
  const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setNewComment(e.target.value)
    e.target.style.height = "auto"
    e.target.style.height = `${e.target.scrollHeight}px`
  }
  return (
    <div className={styles.container}>
      {user && <div className={styles.commentInputSection}>
        <Avatar className={styles.avatar}>
          <AvatarImage src={user.picture ?? ''} />
          <AvatarFallback>{user?.name?.substring(0, 3).toUpperCase()}</AvatarFallback>
        </Avatar>
        <Textarea
          value={newComment}
          onChange={handleTextareaChange}
          placeholder="Add your comment"
          className={styles.textarea}
          rows={1}
        />
        <Button
          size="icon"
          className={styles.button}
          onClick={handleSubmitComment}
          disabled={isLoading || !newComment.trim()}
        >
          <span>Send</span>
        </Button>
      </div>}

      <div className={styles.commentsList} ref={commentListRef}>
        {comments.map((comment) => (
          <CommentItem key={comment.id} comment={comment} />
        ))}
      </div>
    </div>
  );
}

export default function CommentItem({ comment }: { comment: PlaceReview }) {
  return (
    <div className={styles.commentItem}>
      <Avatar className={styles.commentItemAvatar}>
        <AvatarImage src={comment.userAvatar} alt={comment.userName || "User"} />
        <AvatarFallback>{(comment.userName || "U").substring(0, 2).toUpperCase()}</AvatarFallback>
      </Avatar>
      <div className={styles.commentItemContent}>
        <div className={styles.commentItemHeader}>
          <span>{comment.userName || "Anonymous"}</span>
        </div>
        <p className={styles.commentItemText}>{comment.comment}</p>
        <div className={styles.commentItemFooter} title={"Posted on " + format(comment.createdAt, 'dd.MM.yyyy')}>
          <CalendarDays className={styles.calendarIcon} />
          {formatDistanceToNow(new Date(comment.createdAt), { addSuffix: true })}
        </div>
      </div>
    </div>
  );
}