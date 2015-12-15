package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Issue implements Serializable {
    public Integer id;
    public IssueType type;
    public String name;
    public String description;
    public User assignedTo;
    public Project project;
    public IssueCommon parent;
    public Long estimatedTime;
    public List<String> tags;
}

