package self.project.animewatchtrack.validation.validators;

import self.project.animewatchtrack.validation.constraints.NullOrNotEmptyList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 17 déc. 2022.
 */

public class NullOrNotEmptyListValidator implements ConstraintValidator<NullOrNotEmptyList, List> {

    @Override
    public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
        return (list == null || !list.isEmpty());
    }
}
