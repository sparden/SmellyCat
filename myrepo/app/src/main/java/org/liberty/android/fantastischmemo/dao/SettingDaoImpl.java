package org.liberty.android.fantastischmemo.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import org.liberty.android.fantastischmemo.entity.Setting;

import java.sql.SQLException;

public class SettingDaoImpl extends AbstractHelperDaoImpl<Setting, Integer> implements SettingDao {
    public SettingDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Setting> config)
        throws SQLException {
        super(connectionSource, config);
    }

    public SettingDaoImpl(ConnectionSource connectionSource, Class<Setting> clazz)
        throws SQLException {
        super(connectionSource, clazz);
    }

    public void replaceSetting(Setting settings) {
        try {
            deleteById(1);
            create(settings);
            updateId(settings, 1);
        } catch (SQLException e) {
            throw new RuntimeException("Error replacing settings", e);
        }
    }
}

