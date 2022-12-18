package self.project.animewatchtrack.validation.validators;

import self.project.animewatchtrack.validation.constraints.NullOrNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Youssef Kaïdi.
 * created 17 déc. 2022.
 */

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, String> {

    @Override
    public boolean isValid(String parameterToValidate, ConstraintValidatorContext constraintValidatorContext) {
        return (parameterToValidate == null || !parameterToValidate.trim().equals(""));
    }
}
