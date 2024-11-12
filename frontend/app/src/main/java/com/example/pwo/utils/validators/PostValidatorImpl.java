package com.example.pwo.utils.validators;

public class PostValidatorImpl implements PostValidator {

    @Override
    public String validatePostTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "Post title cannot be empty.";
        }
        return null;
    }

    @Override
    public String validatePostDescription(String description) {
        if (description == null || description.isEmpty()) {
            return "Post description cannot be empty.";
        }
        return null;
    }
}
