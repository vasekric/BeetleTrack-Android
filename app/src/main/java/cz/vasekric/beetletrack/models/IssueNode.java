package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasek on 04.10.2015.
 */
public class IssueNode implements Issue, Serializable {
    public Integer id;
    public String name;
    public List<String> tags;
    public User assignedTo;
    public String description;
    public IssueType type;
    public Project project;
    public IssueNode parent;
    public Long estimatedTime;
    public List<Issue> issues = new ArrayList<>();
}
