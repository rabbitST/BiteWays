package com.codecool.biteways.model.validation;

        import com.codecool.biteways.model.enums.UnitType;

        import javax.validation.ConstraintValidator;
        import javax.validation.ConstraintValidatorContext;
        import java.util.Arrays;

public class UnitTypeValidator implements ConstraintValidator<ValidUnitType, String> {

    @Override
    public void initialize(ValidUnitType constraintAnnotation) {
        System.out.println("Value received by validator: initialize");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTt");
        System.out.println("Value received by validator: " + value);
        return Arrays.stream(UnitType.values()).toList().contains(value.toUpperCase());
    }
}
