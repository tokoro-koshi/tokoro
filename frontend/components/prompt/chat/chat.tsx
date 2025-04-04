﻿'use client';

import {
  ReactNode,
  useCallback,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import { Loader2, Search, Send } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Place } from '@/lib/types/place';
import styles from './chat.module.css';
import { useMutation } from '@tanstack/react-query';
import PlaceList from '@/components/cards/place-list/place-list';
import axios from 'axios';
import { ChatMessage } from '@/lib/types/prompt';

interface ChatInterfaceProps {
  children: ReactNode;
}

const PAGINATION_STEP = 6;

export default function ChatInterface({ children }: ChatInterfaceProps) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState('');
  const [lastIndex, setLastIndex] = useState(PAGINATION_STEP);
  const chatBottomRef = useRef<HTMLDivElement>(null);

  useLayoutEffect(() => {
    if (!chatBottomRef.current) return;
    // chatBottomRef.current.scrollIntoView({ behavior: "smooth" });
  }, [lastIndex, messages]);

  const { status, mutate } = useMutation({
    mutationFn: async (input: string) =>
      await axios.post<Place[]>('/api/places/search', { prompt: input }),
    onSuccess: ({ data: fetchedPlaces }) => {
      if (fetchedPlaces) {
        const aiMessage = {
          id: `ai-${Date.now()}`,
          type: 'ai' as const,
          content: '',
          cards: fetchedPlaces,
        };
        setMessages((prev) => [...prev, aiMessage]);
      }
    },
    // TODO: Implement onError
  });
  const isLoading = status === 'pending';

  const handleSendMessage = useCallback(() => {
    if (!input.trim()) return;

    // Add user message
    const userMessage = {
      id: `user-${Date.now()}`,
      type: 'user' as const,
      content: input,
    };

    setMessages((prev) => [...prev, userMessage]);
    setInput('');
    setLastIndex(PAGINATION_STEP);

    mutate(input);
  }, [input, mutate]);

  const handleGenerateMore = async () => {
    if (isLoading) return;
    setLastIndex((prev) => prev + PAGINATION_STEP);
  };

  return (
    <div className={styles.container}>
      {messages.length === 0 ? (
        children
      ) : (
        <div className={styles.messageContainer}>
          {messages.map((message, index) => (
            <div key={message.id} className='w-full'>
              {message.type === 'user' ? (
                <div className={styles.userMessage}>
                  <div className={styles.userMessageContent}>
                    {message.content}
                  </div>
                </div>
              ) : (
                <div className={styles.aiMessage}>
                  {/* AI response cards */}
                  <PlaceList
                    places={message.cards?.slice(0, lastIndex)}
                    noPlacesMessage='No places found'
                  />

                  {/* Generate more button */}
                  {message.cards &&
                    message.cards.length > lastIndex &&
                    index === messages.length - 1 && (
                      <div className='flex justify-center' ref={chatBottomRef}>
                        <Button
                          className={styles.generateMoreButton}
                          onClick={handleGenerateMore}
                          disabled={isLoading}
                        >
                          Generate more
                        </Button>
                      </div>
                    )}
                </div>
              )}
            </div>
          ))}
          {/* Loading indicator */}
          {isLoading && (
            <div className={styles.loadingIndicator}>
              <div className={styles.loadingText}>
                <Loader2 className='h-5 w-5 animate-spin text-muted-foreground' />
                <span className='text-muted-foreground'>
                  Generating results...
                </span>
              </div>
            </div>
          )}
        </div>
      )}

      {/* Input area */}
      <div className={styles.inputArea}>
        <div className={styles.inputContainer}>
          <div className={styles.inputIcon}>
            <Search className='h-5 w-5' />
          </div>
          <Input
            value={input}
            onChange={(event) => setInput(event.target.value)}
            placeholder='Type command or search...'
            className={styles.inputField}
            onKeyDown={(event) => {
              if (event.key !== 'Enter' || isLoading) return;
              handleSendMessage();
            }}
          />
          <Button
            size='icon'
            className={styles.sendButton}
            onClick={handleSendMessage}
            disabled={isLoading || !input.trim()}
          >
            <Send className='h-5 w-5' />
          </Button>
        </div>
      </div>
    </div>
  );
}
