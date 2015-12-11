package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasek on 04.10.2015.
 */
public class Project implements Serializable {
    public Integer id;
    public String name;
    public String readme;
    public User projectManager;
    public List<User> users = new ArrayList<>();
    public List<Issue> issues = new ArrayList<>();
}
