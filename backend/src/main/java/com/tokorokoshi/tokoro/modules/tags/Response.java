package com.tokorokoshi.tokoro.modules.tags;

public class Response<C> {
    private final String conversationId;

    private final C content;
    private final String refusal;

    public Response(
            C content,
            String conversationId
    ) {
        this.content = content;
        this.refusal = null;
        this.conversationId = conversationId;
    }

    public Response(
            String refusal,
            String conversationId
    ) {
        this.content = null;
        this.refusal = refusal;
        this.conversationId = conversationId;
    }

    public C getContent() {
        if (!isSuccessful()) {
            throw new IllegalStateException("Response is not successful");
        }
        return content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getRefusal() {
        if (isSuccessful()) {
            throw new IllegalStateException("Response is successful");
        }
        return refusal;
    }

    public boolean isSuccessful() {
        return content != null;
    }

    public static <C> Builder<C> builder() {
        return new Builder<>();
    }

    public static class Builder<C> {
        protected Builder() {
        }

        private String conversationId;
        private C content;
        private String refusal;

        public Builder<C> conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder<C> content(C content) {
            this.content = content;
            return this;
        }

        public Builder<C> refusal(String refusal) {
            this.refusal = refusal;
            return this;
        }

        public Response<C> build() {
            if (content == null && refusal == null) {
                throw new IllegalStateException("Response must have content or refusal");
            }

            if (content != null && refusal != null) {
                throw new IllegalStateException("Response cannot have both content and refusal");
            }

            if (content != null) {
                return new Response<>(content, conversationId);
            } else {
                return new Response<>(refusal, conversationId);
            }
        }
    }
}
