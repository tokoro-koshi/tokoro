package com.tokorokoshi.tokoro.modules.ai;

import org.json.JSONObject;

public class Response<C> {
    private final String conversationId;

    private final C content;
    private final String refusal;
    private final Integer refusalStatus;

    public Response(
        C content,
        String conversationId
    ) {
        this.content = content;
        this.refusal = null;
        this.refusalStatus = null;
        this.conversationId = conversationId;
    }

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

    public static <C> Builder<C> builder() {
        return new Builder<>();
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

    public Integer getRefusalStatus() {
        if (isSuccessful()) {
            throw new IllegalStateException("Response is successful");
        }
        return refusalStatus;
    }

    public String getJsonRefusal() {
        var jo = new JSONObject();
        jo.put("message", this.refusal);
        jo.put("status", this.refusalStatus);
        return jo.toString();
    }

    public boolean isSuccessful() {
        return content != null;
    }

    public boolean isRefusal() {
        return refusal != null;
    }

    public String toString() {
        if (isSuccessful()) {
            return "Response{content=" + content + "}";
        } else {
            return "Response{refusal=" + refusal + ", status=" + refusalStatus + "}";
        }
    }

    public static class Builder<C> {
        private String conversationId;
        private C content;
        private String refusal;
        private Integer refusalStatus;
        protected Builder() {
        }

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

        public Builder<C> refusalStatus(Integer refusalStatus) {
            this.refusalStatus = refusalStatus;
            return this;
        }

        public Builder<C> refusalStatus(String refusalStatus) {
            this.refusalStatus = refusalStatus == null
                ? 500
                : Integer.parseInt(refusalStatus);
            return this;
        }

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
