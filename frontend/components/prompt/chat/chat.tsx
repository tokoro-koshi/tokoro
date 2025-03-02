'use client';

import { useCallback, useState } from 'react';
import { Loader2, Search, Send } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Place } from '@/lib/types/place';
import styles from './chat.module.css';
import { useMutation } from '@tanstack/react-query';
import { PlaceClient } from '@/lib/requests/place.client';
import PlaceList from '@/components/cards/place-list/place-list';

interface ChatInterfaceProps {
  children: React.ReactNode;
}

const PAGINATION_STEP = 6;

export default function ChatInterface({ children }: ChatInterfaceProps) {
  const [messages, setMessages] = useState<
    Array<{
      id: string;
      type: 'user' | 'ai';
      content: string;
      cards?: Array<Place>;
    }>
  >([]);
  const [input, setInput] = useState('');
  const [lastIndex, setLastIndex] = useState(PAGINATION_STEP);

  const { status, mutate } = useMutation({
    mutationFn: async (input: string) => await PlaceClient.searchPlaces(input),
    onSuccess: (fetchedPlaces) => {
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
                      <div className='flex justify-center'>
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
            onChange={(e) => setInput(e.target.value)}
            placeholder='Type command or search...'
            className={styles.inputField}
            onKeyDown={(e) => {
              if (e.key === 'Enter' && !isLoading) {
                handleSendMessage();
              }
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
