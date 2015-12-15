package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasek on 04.10.2015.
 */
public class IssueNode extends Issue implements Serializable {
    public List<Issue> children = new ArrayList<>();
}
