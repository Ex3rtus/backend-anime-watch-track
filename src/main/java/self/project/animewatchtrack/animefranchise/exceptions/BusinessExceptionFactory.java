package self.project.animewatchtrack.animefranchise.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Youssef Kaïdi.
 * created 01 nov. 2022.
 */

public class BusinessExceptionFactory {
    private static final Map<String, Function<String, BusinessException>> factoryMap =
            Collections.unmodifiableMap(new HashMap<>() {{
                put("FRANCHISE_EXISTS", message -> new AnimeFranchiseAlreadyExistsException(message));
                put("FRANCHISE_NOT_FOUND", message -> new AnimeFranchiseNotFoundException(message));
            }});

    public static BusinessException getException(String exceptionType, String message) {
        return factoryMap.get(exceptionType).apply(message);
    }
}
