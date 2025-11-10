package com.prototypeportal.resource;

import com.prototypeportal.exception.BusinessException;
import com.prototypeportal.exception.EmailAlreadyExistsException;
import com.prototypeportal.exception.InvalidCredentialsException;
import com.prototypeportal.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

/**
 * グローバル例外ハンドラー
 *
 * すべての例外をキャッチして適切なHTTPレスポンスに変換
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.severe("Exception caught: " + exception.getClass().getName() + " - " + exception.getMessage());

        // ResourceNotFoundException -> 404
        if (exception instanceof ResourceNotFoundException) {
            BusinessException be = (BusinessException) exception;
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.error(be.getMessage(), be.getErrorCode()))
                .build();
        }

        // EmailAlreadyExistsException -> 409 Conflict
        if (exception instanceof EmailAlreadyExistsException) {
            BusinessException be = (BusinessException) exception;
            return Response.status(Response.Status.CONFLICT)
                .entity(ApiResponse.error(be.getMessage(), be.getErrorCode()))
                .build();
        }

        // InvalidCredentialsException -> 401 Unauthorized
        if (exception instanceof InvalidCredentialsException) {
            BusinessException be = (BusinessException) exception;
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ApiResponse.error(be.getMessage(), be.getErrorCode()))
                .build();
        }

        // BusinessException -> 400 Bad Request
        if (exception instanceof BusinessException) {
            BusinessException be = (BusinessException) exception;
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.error(be.getMessage(), be.getErrorCode()))
                .build();
        }

        // その他の例外 -> 500 Internal Server Error
        LOGGER.severe("Unhandled exception: " + exception.getMessage());
        exception.printStackTrace();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR"))
            .build();
    }
}
