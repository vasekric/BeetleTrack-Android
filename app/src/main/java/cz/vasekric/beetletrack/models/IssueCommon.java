package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasek on 11.12.2015.
 */
public class IssueCommon extends Issue implements Serializable {
    public List<SpendTime> spentTime;
    public List<String> tags;
    public List<IssueCommon> children = new ArrayList<>();
    public String clazz;
}
