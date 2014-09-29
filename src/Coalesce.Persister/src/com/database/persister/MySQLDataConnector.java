package com.database.persister;

import java.sql.DriverManager;
import java.sql.SQLException;

import Coalesce.Common.Exceptions.CoalescePersistorException;

public class MySQLDataConnector extends CoalesceDataConnectorBase {

    public MySQLDataConnector(ServerConn settings) throws CoalescePersistorException
    {
        try
        {
            _settings = settings;

            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new CoalescePersistorException("CoalesceDataConnector", e);
        }
    }

    @Override
    public void openConnection() throws SQLException
    {
        this._settings.setPostGres(false);
        this._conn = DriverManager.getConnection(this._settings.getURL(),
                                                 this._settings.getUser(),
                                                 this._settings.getPassword());
    }

    @Override
    protected String getProcedurePrefix()
    {
        return "call coalescedatabase.";
    }

}
