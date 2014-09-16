package com.database.persister;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import Coalesce.Common.Exceptions.CoalescePersistorException;

public class PostGresDataConnector implements AutoCloseable {

    /*--------------------------------------------------------------------------
    Private Member Variables
    --------------------------------------------------------------------------*/

    Connection _conn = null;
    private ServerConn _serCon = null;

    /*--------------------------------------------------------------------------
    Constructors
    --------------------------------------------------------------------------*/

    public PostGresDataConnector(ServerConn settings) throws CoalescePersistorException
    {
        this._serCon = settings;
        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new CoalescePersistorException("PostGresDataConnector", e);
        }
    }

    /*--------------------------------------------------------------------------
    Public Functions
    --------------------------------------------------------------------------*/

    public void OpenConnection() throws SQLException
    {
        this._serCon.setPostGres(true);
        this._conn = DriverManager.getConnection(this._serCon.getURL(), this._serCon.getUser(), this._serCon.getPassword());
    }

    public void CloseConnection() throws Exception
    {
        this.close();
    }

    public ResultSet ExecuteQuery(String SQL, String... parameters) throws SQLException
    {

        // Open Connection if not already created
        if (this._conn == null) this.OpenConnection();

        CallableStatement stmt = this._conn.prepareCall(SQL);

        // Add Parameters
        for (int ii = 0; ii < parameters.length; ii++)
        {
            stmt.setString(ii + 1, parameters[ii].trim());
        }

        return stmt.executeQuery();

    }

    public ResultSet ExecuteQuery(String SQL) throws SQLException
    {

        // Open Connection if not already created
        if (this._conn == null) this.OpenConnection();

        CallableStatement stmt = this._conn.prepareCall(SQL);

        return stmt.executeQuery();

    }

    public boolean ExecuteCmd(String SQL, String... parameters) throws SQLException
    {

        // Open Connection if not already created
        if (this._conn == null) this.OpenConnection();

        CallableStatement stmt = this._conn.prepareCall(SQL);

        // Add Parameters
        for (int ii = 0; ii < parameters.length; ii++)
        {
            stmt.setString(ii + 1, parameters[ii].trim());
        }

        stmt.executeUpdate();

        return true;

    }

    public boolean ExecuteCmd(String SQL) throws SQLException
    {

        // Open Connection if not already created
        if (this._conn == null) this.OpenConnection();

        CallableStatement stmt = this._conn.prepareCall(SQL);

        stmt.executeUpdate();

        return true;

    }

    public boolean ExecuteProcedure(String procedureName, String... parameters) throws SQLException
    {

        // Compile SQL Command
        StringBuilder sb = new StringBuilder("{call public." + procedureName + " (");

        for (int ii = 0; ii < parameters.length; ii++)
        {
            if (ii != 0) sb.append(",");
            sb.append("?");
        }

        sb.append(")}");

        // TODO: Implement Retry

        // Open Connection if not already created
        if (this._conn == null) this.OpenConnection();

        CallableStatement stmt = this._conn.prepareCall(sb.toString());

        // Add Parameters
        for (int ii = 0; ii < parameters.length; ii++)
        {
            stmt.setString(ii + 1, parameters[ii].trim());
        }

        stmt.executeUpdate();

        return true;
    }

    /*--------------------------------------------------------------------------
    Finalize
    --------------------------------------------------------------------------*/

    @Override
    public void close() throws SQLException
    {
        if (this._conn != null)
        {
            if (!this._conn.getAutoCommit()) this._conn.commit();
            this._conn.close();
        }
    }
}