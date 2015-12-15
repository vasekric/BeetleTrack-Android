package cz.vasekric.beetletrack.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vasek on 04.10.2015.
 */
public class IssueLeaf extends Issue implements Serializable {
    public List<SpendTime> spentTime;
}
