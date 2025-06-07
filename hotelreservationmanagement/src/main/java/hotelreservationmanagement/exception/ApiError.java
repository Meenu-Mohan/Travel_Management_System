package hotelreservationmanagement.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private String errorCode; 


    private static final Logger logger = LogManager.getLogger(ApiError.class);

    public ApiError(HttpStatus status, String message, String details) {
        this(status, message, details, null);
    }

  
    public ApiError(HttpStatus status, String message, String details, String errorCode) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
        logger.debug("Created ApiError: status={}, message={}, errorCode={}", status, message, errorCode);
    }
}