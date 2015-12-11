package cz.vasekric.beetletrack.models;

import java.io.Serializable;

/**
 * Created by vasek on 03.10.2015.
 */
public class User implements Serializable {
    public Integer id;
    public String username;
    public String fullName;
    public String email;
    public String password;
    public boolean  authenticated;
}
