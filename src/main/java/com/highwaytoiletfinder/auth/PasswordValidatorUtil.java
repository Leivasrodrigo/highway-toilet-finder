package com.highwaytoiletfinder.auth;

import org.passay.*;

import java.util.Arrays;

public class PasswordValidatorUtil {

    private static final PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new LengthRule(8, 128),
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            new CharacterRule(EnglishCharacterData.Special, 1),
            new WhitespaceRule()
    ));

    public static void validate(String password) {
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            throw new IllegalArgumentException(String.join(", ", validator.getMessages(result)));
        }
    }
}
