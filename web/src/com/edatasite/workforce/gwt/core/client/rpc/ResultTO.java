package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultTO<T> {

    private static final ResultTO<?> SUCCESS = ResultTO.builder().success(true).build();

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("data")
    private T data;
    @JsonProperty("error")
    private ErrorTO error;

    public ResultTO() {
    }

    public ResultTO(boolean success, T data, ErrorTO error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ErrorTO getError() {
        return error;
    }

    public static ResultTO<?> success() {
        return SUCCESS;
    }

    public static <T> ResultTO<T> success(final T data) {
        return ResultTO.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ResultTO<T> failure(final String errorMessage, final int errorCode) {
        return ResultTO.<T>builder()
                .success(false)
                .error(new ErrorTO(errorMessage, errorCode))
                .build();
    }

    public static <T> ResultTO<T> fromOptional(final Optional<T> optData) {
        return optData.map(ResultTO::success).orElse(ResultTO.failure("Data not found", ApiConstants.NOT_FOUND));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultTO)) return false;

        ResultTO<?> resultTO = (ResultTO<?>) o;

        if (isSuccess() != resultTO.isSuccess()) return false;
        if (getData() != null ? !getData().equals(resultTO.getData()) : resultTO.getData() != null) return false;
        if (getError() != null ? !getError().equals(resultTO.getError()) : resultTO.getError() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isSuccess() ? 1 : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        result = 31 * result + (getError() != null ? getError().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResultTO{" +
                "success=" + success +
                ", data=" + data +
                ", error=" + error +
                '}';
    }

    public static class ErrorTO {
        private @JsonProperty("user_msg")
        String message;
        private @JsonProperty("error_code")
        int code;

        public ErrorTO() {
        }

        public ErrorTO(String message, int code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ErrorTO)) return false;

            ErrorTO errorTO = (ErrorTO) o;

            if (getCode() != errorTO.getCode()) return false;
            if (getMessage() != null ? !getMessage().equals(errorTO.getMessage()) : errorTO.getMessage() != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = getMessage() != null ? getMessage().hashCode() : 0;
            result = 31 * result + getCode();
            return result;
        }

        @Override
        public String toString() {
            return "ErrorTO{" +
                    "message='" + message + '\'' +
                    ", code=" + code +
                    '}';
        }
    }

    public static <T> ResultTOBuilder<T> builder() {
        return new ResultTOBuilder<>();
    }

    public static class ResultTOBuilder<T> {

        private boolean success;
        private T data;
        private ErrorTO error;

        public ResultTOBuilder() {
        }

        public ResultTOBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ResultTOBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResultTOBuilder<T> error(ErrorTO error) {
            this.error = error;
            return this;
        }

        public ResultTO<T> build() {
            return new ResultTO<>(this.success, this.data, this.error);
        }

    }
}