package self.project.animewatchtrack.validation.validators;

import self.project.animewatchtrack.validation.constraints.NullOrPositive;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Youssef Kaïdi.
 * created 17 déc. 2022.
 */

public class NullOrPositiveValidator implements ConstraintValidator<NullOrPositive, Integer> {

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return (integer == null || integer >= 0);
    }
}
