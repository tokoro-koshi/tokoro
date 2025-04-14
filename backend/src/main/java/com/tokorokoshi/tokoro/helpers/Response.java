package com.tokorokoshi.tokoro.helpers;

import org.json.JSONObject;

/**
 * A custom response object that can either contain a successful response or a refusal.
 *
 * @param <C> The type of the content of the response
 */
public class Response<C> {
    private final String conversationId;

    private final C content;
    private final String refusal;
    private final Integer refusalStatus;

    /**
     * Create a successful response.
     *
     * @param content        The content of the response
     * @param conversationId The conversation ID of the response
     */
    public Response(
            C content,
            String conversationId
    ) {
        this.content = content;
        this.refusal = null;
        this.refusalStatus = null;
        this.conversationId = conversationId;
    }

    /**
     * Create a refusal response.
     *
     * @param refusal        The refusal message
     * @param refusalStatus  The refusal status code
     * @param conversationId The conversation ID of the response
     */
    public Response(
            String refusal,
            Integer refusalStatus,
            String conversationId
    ) {
        this.content = null;
        this.refusal = refusal;
        this.refusalStatus = refusalStatus;
        this.conversationId = conversationId;
    }

    /**
     * Create a builder for the response.
     *
     * @param <C> The type of the content of the response
     * @return The builder
     */
    public static <C> Builder<C> builder() {
        return new Builder<>();
    }

    /**
     * Get the content of the response.
     *
     * @return The content
     */
    public C getContent() {
        if (!isSuccessful()) {
            throw new IllegalStateException("Response is not successful");
        }
        return content;
    }

    /**
     * Get the conversation ID of the response.
     *
     * @return The conversation ID
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Get the refusal message of the response.
     *
     * @return The refusal message
     */
    public String getRefusal() {
        if (isSuccessful()) {
            throw new IllegalStateException("Response is successful");
        }
        return refusal;
    }

    /**
     * Get the refusal status code of the response.
     *
     * @return The refusal status code
     */
    public Integer getRefusalStatus() {
        if (isSuccessful()) {
            throw new IllegalStateException("Response is successful");
        }
        return refusalStatus;
    }

    /**
     * Get the JSON representation of the response.
     *
     * @return The JSON representation
     */
    public String getJsonRefusal() {
        var jo = new JSONObject();
        jo.put("message", this.refusal);
        jo.put("status", this.refusalStatus);
        return jo.toString();
    }

    /**
     * Check if the response is successful.
     *
     * @return Whether the response is successful
     */
    public boolean isSuccessful() {
        return content != null;
    }

    /**
     * Check if the response is a refusal.
     *
     * @return Whether the response is a refusal
     */
    public boolean isRefusal() {
        return refusal != null;
    }

    /**
     * Convert the response to a string. Useful for debugging purposes.
     */
    @Override
    public String toString() {
        if (isSuccessful()) {
            return "Response{content=" + content + "}";
        } else {
            return "Response{refusal=" + refusal + ", status=" + refusalStatus + "}";
        }
    }

    /**
     * A builder for the response.
     *
     * @param <C> The type of the content of the response
     */
    public static class Builder<C> {
        private String conversationId;
        private C content;
        private String refusal;
        private Integer refusalStatus;

        protected Builder() {
        }

        /**
         * Set the conversation ID of the response.
         *
         * @param conversationId The conversation ID
         * @return The builder
         */
        public Builder<C> conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        /**
         * Set the content of the response.
         *
         * @param content The content
         * @return The builder
         */
        public Builder<C> content(C content) {
            this.content = content;
            return this;
        }

        /**
         * Set the refusal message of the response.
         *
         * @param refusal The refusal message
         * @return The builder
         */
        public Builder<C> refusal(String refusal) {
            this.refusal = refusal;
            return this;
        }

        /**
         * Set the refusal status code of the response.
         *
         * @param refusalStatus The refusal status code
         * @return The builder
         */
        public Builder<C> refusalStatus(Integer refusalStatus) {
            this.refusalStatus = refusalStatus;
            return this;
        }

        /**
         * Set the refusal status code of the response.
         *
         * @param refusalStatus The refusal status code
         * @return The builder
         */
        public Builder<C> refusalStatus(String refusalStatus) {
            this.refusalStatus = refusalStatus == null
                                 ? 500
                                 : Integer.parseInt(refusalStatus);
            return this;
        }

        /**
         * Build the response.
         *
         * @return The response
         */
        public Response<C> build() {
            if (content == null && refusal == null) {
                throw new IllegalStateException(
                        "Response must have content or refusal");
            }

            if (content != null && (refusal != null || refusalStatus != null)) {
                throw new IllegalStateException(
                        "Response cannot have both content and refusal");
            }

            if (content != null) {
                return new Response<>(content, conversationId);
            } else {
                return new Response<>(refusal, refusalStatus, conversationId);
            }
        }
    }
}
