package com.mike.demo.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidatorTest {

    private final EmailValidator underTest = new EmailValidator();

    @Test
    void itShouldValidateCorrectEmail() {
        assertThat(underTest.test("hello@gmail.com")).isTrue();
    }

    @Test
    void itShouldValidateAnIncorrectEmail() {
        assertThat(underTest.test("hellogmail.com")).isFalse();
    }

    @Test
    void itShouldValidateAnIncorrectEmailWithoutDitAtTheEnd() {
        assertThat(underTest.test("hello@gmailcom")).isFalse();
    }
}