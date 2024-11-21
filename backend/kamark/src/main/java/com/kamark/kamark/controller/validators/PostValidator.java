package com.kamark.kamark.controller.validators;

public class PostValidator implements PostValidatorInterface {
    private static final int MAX_TITLE_LENGTH = 40;
    private static final int MAX_DESCRIPTION_LENGTH = 2000;
    @Override
    public String validatePostTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "Post title cannot be empty.";
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            return "Post title cannot exceed " + MAX_TITLE_LENGTH + " characters.";
        }
        return null;
    }

    @Override
    public String validatePostDescription(String description) {
        if (description == null || description.isEmpty()) {
            return "Post description cannot be empty.";
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            return "Post title cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters.";
        }
        return null;
    }
}
