package model;

import lombok.Data;
import org.noear.solon.annotation.Param;

import java.util.Date;

@Data
public class UserModel {
    public int id;
    public String name;
    public int sex;

    public transient String _type;

    @Param(format = "yyyy-MM-dd")
    public Date date = new Date();

    public long[] aaa;
}
