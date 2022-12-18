package self.project.animewatchtrack.validation.constraints;

import self.project.animewatchtrack.validation.validators.EpisodeNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Youssef Kaïdi.
 * created 15 déc. 2022.
 */

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = EpisodeNumberValidator.class)
@Documented
public @interface EpisodeNumber {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
