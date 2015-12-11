package cz.vasekric.beetletrack.models;

import java.io.Serializable;

/**
 * Created by vasek on 18.11.2015.
 */
public enum IssueType implements Serializable {
    EPIC("epic"),
    USER_STORY("user_story"),
    TASK("task");

    private String text;

    IssueType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static IssueType fromString(String text) {
        if (text != null) {
            for (IssueType b : IssueType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
