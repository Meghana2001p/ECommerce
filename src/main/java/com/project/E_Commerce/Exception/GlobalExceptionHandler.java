//package com.project.E_Commerce.Exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.NoHandlerFoundException;
//
//import java.time.LocalDateTime;
//
//@RestController
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    // User-related exceptions
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST
//                        .value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(UserAlreadyExists.class)
//    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExists exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    // Client-related exceptions
//    @ExceptionHandler(ClientNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(NoUserFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNoUserFoundException(NoUserFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(ClientAlreadyExists.class)
//    public ResponseEntity<ErrorResponse> handleClientAlreadyExists(ClientAlreadyExists exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    // Partner-related exceptions
//    @ExceptionHandler(PartnerNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handlePartnerNotFoundException(PartnerNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(PartnerAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handlePartnerAlreadyExistsException(PartnerAlreadyExistsException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    // Data operation exceptions
//    @ExceptionHandler(DataCreationException.class)
//    public ResponseEntity<ErrorResponse> handleDataCreationException(DataCreationException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(DataRetrievalException.class)
//    public ResponseEntity<ErrorResponse> handleDataRetrievalException(DataRetrievalException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(DataDeletionException.class)
//    public ResponseEntity<ErrorResponse> handleDataDeletionException(DataDeletionException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(DataUpdateException.class)
//    public ResponseEntity<ErrorResponse> handleDataUpdateException(DataUpdateException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    // Resource exceptions
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(DuplicateResourceException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(DuplicateRelationException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateRelationException(DuplicateRelationException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    // Authentication/Authorization exceptions
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.UNAUTHORIZED.value(),  // Changed from CONFLICT to UNAUTHORIZED
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//    }
//
//    // Validation exceptions
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//    //UserDeactivationException
//    @ExceptionHandler(UserDeactivationException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(UserDeactivationException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//    @ExceptionHandler(PreferenceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handlePreferenceNotFoundException(
//            PreferenceNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(DuplicatePreferenceException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicatePreferenceException(
//            DuplicatePreferenceException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//
//    @ExceptionHandler(ServiceException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            ServiceException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
////NoContentException
//
//    @ExceptionHandler(NoContentException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            NoContentException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
////ProductSearchException
//
//    @ExceptionHandler(ProductSearchException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            ProductSearchException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
////InvalidSearchCriteriaException
//
//
//    @ExceptionHandler(InvalidSearchCriteriaException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            InvalidSearchCriteriaException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
////CartNotFoundException
//
//
//    @ExceptionHandler(CartNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            CartNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
////ProductNotFoundException
//
//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            ProductNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//
//    //CouponNotFoundException
//    @ExceptionHandler(CouponNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            CouponNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
////InvalidCouponException
//
//
//    @ExceptionHandler(InvalidCouponException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            InvalidCouponException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
////CouponAlreadyExistsException
//@ExceptionHandler(CouponAlreadyExistsException.class)
//public ResponseEntity<ErrorResponse> handleServiceException(
//        CouponAlreadyExistsException exception, HttpServletRequest request) {
//    ErrorResponse error = new ErrorResponse(
//            LocalDateTime.now(),
//            HttpStatus.BAD_REQUEST.value(),
//            exception.getMessage()
//    );
//    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//}
//
////OrderCouldNotBePlacedException
//@ExceptionHandler(OrderCouldNotBePlacedException.class)
//public ResponseEntity<ErrorResponse> handleServiceException(
//        OrderCouldNotBePlacedException exception, HttpServletRequest request) {
//    ErrorResponse error = new ErrorResponse(
//            LocalDateTime.now(),
//            HttpStatus.BAD_REQUEST.value(),
//            exception.getMessage()
//    );
//    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//}
//
////OrderValidationException
//
//    @ExceptionHandler(OrderValidationException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            OrderValidationException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//
//    //DeliveryStatusCreationException
//    @ExceptionHandler(DeliveryStatusCreationException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            DeliveryStatusCreationException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//
//   //OrderCannotCancelException
//   @ExceptionHandler(OrderCannotCancelException.class)
//   public ResponseEntity<ErrorResponse> handleServiceException(
//           OrderCannotCancelException exception, HttpServletRequest request) {
//       ErrorResponse error = new ErrorResponse(
//               LocalDateTime.now(),
//               HttpStatus.BAD_REQUEST.value(),
//               exception.getMessage()
//       );
//       return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//   }
//
//
//   //DataBaseException
//
//
//    @ExceptionHandler(DataBaseException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            DataBaseException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    //OrderNotFoundException
//
//    @ExceptionHandler(OrderNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            OrderNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    //ReviewNotFoundException
//
//
//    @ExceptionHandler(ReviewNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleServiceException(
//            ReviewNotFoundException exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//
//
//
//    // Generic exception handler
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                exception.getMessage()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}