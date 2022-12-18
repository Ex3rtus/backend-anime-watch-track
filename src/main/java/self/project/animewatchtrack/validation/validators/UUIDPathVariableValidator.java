package self.project.animewatchtrack.validation.validators;

import self.project.animewatchtrack.validation.constraints.UUIDURI;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Youssef Kaïdi.
 * created 17 déc. 2022.
 */

public class UUIDPathVariableValidator implements ConstraintValidator<UUIDURI, String> {

    @Override
    public boolean isValid(String uuidToValidate, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(uuidToValidate).matches();
    }
}
