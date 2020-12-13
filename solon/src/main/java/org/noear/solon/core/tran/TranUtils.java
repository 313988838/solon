package org.noear.solon.core.tran;

import org.noear.solon.annotation.Note;
import org.noear.solon.core.Bridge;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务工具
 *
 * @author noear
 * @since 1.0
 * */
public class TranUtils {
    /**
     * 是否在事务中
     */
    @Note("是否在事务中")
    public static boolean inTrans() {
        return Bridge.tranExecutor().inTrans();
    }

    /**
     * 是否在事务中且只读
     */
    @Note("是否在事务中且只读")
    public static boolean inTransAndReadOnly() {
        return Bridge.tranExecutor().inTransAndReadOnly();
    }

    /**
     * 获取链接
     */
    @Note("获取链接")
    public static Connection getConnection(DataSource ds) throws SQLException {
        return Bridge.tranExecutor().getConnection(ds);
    }
}
