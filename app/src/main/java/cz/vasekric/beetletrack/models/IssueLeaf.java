package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vasek on 04.10.2015.
 */
public class IssueLeaf implements Issue, Serializable {
    public Integer id;
    public IssueType type;
    public String name;
    public String description;
    public User assignedTo;
    public Project project;
    public IssueNode parent;
    public List<SpendTime> spentTime;
    public Long estimatedTime;
}
