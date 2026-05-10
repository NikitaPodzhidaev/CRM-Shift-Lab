package shift.lab.crm.common.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDto(

        int errorValue,
        String message,
        List<String> errorDetails,
        LocalDateTime errorTime

) {
}
