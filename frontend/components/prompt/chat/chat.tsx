"use client"

import { useState } from "react"
import { Search, Send, Loader2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/cards/place-card/place-card';
import { PlaceClient } from '@/lib/requests/place.client';
import styles from './chat.module.css';

interface ChatInterfaceProps {
  children: React.ReactNode;
}

export default function ChatInterface({ children }: ChatInterfaceProps) {

  const [messages, setMessages] = useState<
    Array<{
      id: string
      type: "user" | "ai"
      content: string
      cards?: Array<Place>
    }>
  >([])
  const [input, setInput] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const handleSendMessage = async () => {
    if (!input.trim()) return

    // Add user message
    const userMessage = {
      id: `user-${Date.now()}`,
      type: "user" as const,
      content: input,
    }

    const places = await PlaceClient.getRandomPlaces(3);

    setMessages((prev) => [...prev, userMessage])
    setInput("")
    setIsLoading(true)

    // Simulate API call
    setTimeout(() => {
      const aiMessage = {
        id: `ai-${Date.now()}`,
        type: "ai" as const,
        content: "",
        cards: places
      }

      setMessages((prev) => [...prev, aiMessage])
      setIsLoading(false)
    }, 1500)
  }

  const handleGenerateMore = async () => {
    if (isLoading) return;

    setIsLoading(true);

    // Simulate API call for more results
    const lastMessage = messages[messages.length - 1];
    if (lastMessage.type === "ai" && lastMessage.cards) {
      const moreCards = await PlaceClient.getRandomPlaces(3);

      setMessages((prev) =>
        prev.map((msg) =>
          msg.id === lastMessage.id ? { ...msg, cards: [...(msg.cards || []), ...moreCards] } : msg,
        ),
      );
    }

    setIsLoading(false);
  };

  return (
    <div className={styles.container}>
      {messages.length === 0 ? children : <div className={styles.messageContainer}>{messages.map((message) => (
        <div key={message.id} className="w-full">
          {message.type === "user" ? (
            <div className={styles.userMessage}>
              <div className={styles.userMessageContent}>{message.content}</div>
            </div>
          ) : (
            <div className={styles.aiMessage}>
              {/* AI response cards */}
              <div className={styles.aiMessageCards}>
                {message.cards?.map((card) => (
                  <PlaceCard place={card} key={card.id} />
                ))}
              </div>

              {/* Generate more button */}
              {message.cards && (
                <div className="flex justify-center">
                  <Button className={styles.generateMoreButton} onClick={handleGenerateMore} disabled={isLoading}>
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
              <Loader2 className="h-5 w-5 animate-spin text-muted-foreground" />
              <span className="text-muted-foreground">Generating results...</span>
            </div>
          </div>
        )}
      </div>}



      {/* Input area */}
      <div className={styles.inputArea}>
        <div className={styles.inputContainer}>
          <div className={styles.inputIcon}>
            <Search className="h-5 w-5" />
          </div>
          <Input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Type command or search..."
            className={styles.inputField}
            onKeyDown={(e) => {
              if (e.key === "Enter" && !isLoading) {
                handleSendMessage()
              }
            }}
          />
          <Button
            size="icon"
            className={styles.sendButton}
            onClick={handleSendMessage}
            disabled={isLoading || !input.trim()}
          >
            <Send className="h-5 w-5" />
          </Button>
        </div>
      </div>
    </div>
  )
}