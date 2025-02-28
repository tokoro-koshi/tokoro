"use client"

import { useState } from "react"
import { Search, Send, Loader2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Place } from '@/lib/types/place';
import PlaceCard from '@/components/cards/place-card';
import { PlaceClient } from '@/lib/requests/place.client';

export default function ChatInterface() {
  
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
    <div className="flex flex-col svg-background min-h-[calc(100vh-59px)]">
      {/* Chat messages area */}
      <div className="flex-1 p-4 space-y-4">
        {messages.map((message) => (
          <div key={message.id} className="w-full">
            {message.type === "user" ? (
              <div className="flex justify-center w-full">
                <div className="bg-background text-primary-foreground p-3 rounded-md max-w-[80%]">{message.content}</div>
              </div>
            ) : (
              <div className="space-y-4">
                {/* AI response cards */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  {message.cards?.map((card) => (
                    <PlaceCard place={card} key={card.id} />
                  ))}
                </div>

                {/* Generate more button */}
                {message.cards && (
                  <div className="flex justify-center">
                    <Button className="bg-foreground text-background rounded-md py-2 px-8" onClick={handleGenerateMore} disabled={isLoading}>
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
          <div className="flex justify-center">
            <div className="flex items-center space-x-2">
              <Loader2 className="h-5 w-5 animate-spin text-muted-foreground" />
              <span className="text-muted-foreground">Generating results...</span>
            </div>
          </div>
        )}
      </div>

      {/* Input area */}
      <div className="p-3 sticky bottom-0">
        <div className="relative max-w-4xl mx-auto">
          <div className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-500">
            <Search className="h-5 w-5" />
          </div>
          <Input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Type command or search..."
            className="bg-white placeholder:text-zinc-400 pl-10 pr-12 py-4 rounded-md"
            onKeyDown={(e) => {
              if (e.key === "Enter" && !isLoading) {
                handleSendMessage()
              }
            }}
          />
          <Button
            size="icon"
            className="absolute right-2 top-1/2 -translate-y-1/2"
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

