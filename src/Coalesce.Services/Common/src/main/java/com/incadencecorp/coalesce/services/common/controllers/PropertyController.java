package com.incadencecorp.coalesce.services.common.controllers;

import com.incadencecorp.coalesce.api.CoalesceErrors;
import com.incadencecorp.coalesce.api.CoalesceParameters;
import com.incadencecorp.coalesce.api.ICoalesceNotifier;
import com.incadencecorp.coalesce.notification.impl.Log4jNotifierImpl;
import com.incadencecorp.coalesce.services.common.api.IPropertyController;
import com.incadencecorp.unity.common.IConfigurationsConnector;
import com.incadencecorp.unity.common.SettingsBase;
import com.incadencecorp.unity.common.connectors.FilePropertyConnector;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This controller provides access to properties defined by the server such as URLs.
 *
 * @author Derek Clemenzi
 */
public class PropertyController implements IPropertyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyController.class);

    private IConfigurationsConnector connector = new FilePropertyConnector(CoalesceParameters.COALESCE_CONFIG_LOCATION);
    private SettingsBase settings = new SettingsBase(connector);
    private String key = "client.properties";
    private boolean isReadOnly = false;
    private ICoalesceNotifier notifier = new Log4jNotifierImpl();

    /**
     * Sets the connector to use when getting / setting properties.
     *
     * @param connector
     */
    public void setConnector(IConfigurationsConnector connector)
    {
        this.connector = connector;
        this.settings = new SettingsBase(connector);
    }

    /**
     * Sets the config key used to store properties. This depends on the connector used but usually this maps to a file name.
     *
     * @param key
     */
    public void setKey(String key)
    {

        this.key = key;
    }

    /**
     * Sets whether or not a client can update settings defined on the server.
     *
     * @param value
     */
    public void setIsReadOnly(boolean value)
    {
        this.isReadOnly = value;
    }

    /**
     * @param notifier to use for sending update notifications.
     */
    public void setNotifier(ICoalesceNotifier notifier)
    {
        Bundle bundle = FrameworkUtil.getBundle(PropertyController.class);

        if (bundle != null)
        {
            notifier.setContext(bundle.getBundleContext());
        }

        this.notifier = notifier;
    }

    @Override
    public String getProperty(String name) throws RemoteException
    {
        return settings.getSetting(key, name, "", false);
    }

    @Override
    public void setProperty(String name, String value) throws RemoteException
    {
        setProperties(Collections.singletonMap(name, value));
    }

    @Override
    public Map<String, String> getProperties() throws RemoteException
    {
        return connector.getSettings(key);
    }

    @Override
    public Map<String, String> getProperties(String[] names) throws RemoteException
    {
        Map<String, String> results = new HashMap<>();

        for (String name : names)
        {
            results.put(name, settings.getSetting(key, name, "", false));
        }

        return results;
    }

    @Override
    public void setProperties(Map<String, String> values) throws RemoteException
    {
        if (isReadOnly)
        {
            throw new RemoteException(String.format(CoalesceErrors.NOT_SAVED,
                                                    "Properties",
                                                    "Map<String, String>",
                                                    "Read Only"));
        }

        for (Map.Entry<String, String> entry : values.entrySet())
        {
            settings.setSetting(key, entry.getKey(), entry.getValue());

            sendMessage(key, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String getJsonConfiguration(String name) throws RemoteException
    {
        JSONObject json;

        try
        {
            Path path = Paths.get(CoalesceParameters.COALESCE_CONFIG_LOCATION).resolve(name + ".json");
            try (InputStream stream = path.toUri().toURL().openStream())
            {
                json = new JSONObject(new JSONTokener(stream));
            }
        }
        catch (IOException e)
        {
            throw new RemoteException(String.format(CoalesceErrors.NOT_FOUND, name, e.getMessage()), e);
        }

        return json.toString();
    }

    @Override
    public void setJsonConfiguration(String name, String json) throws RemoteException
    {
        if (isReadOnly)
        {
            throw new RemoteException(String.format(CoalesceErrors.NOT_SAVED,
                                                    name,
                                                    JSONObject.class.getSimpleName(),
                                                    "Read Only"));
        }

        try
        {
            Path path = Paths.get(CoalesceParameters.COALESCE_CONFIG_LOCATION).resolve(name + ".json");
            try (FileWriter writer = new FileWriter(path.toString()))
            {
                writer.write(json);
            }

            sendMessage(name, name, json);
        }
        catch (IOException e)
        {
            throw new RemoteException(String.format(CoalesceErrors.NOT_SAVED,
                                                    name,
                                                    JSONObject.class.getSimpleName(),
                                                    e.getMessage()), e);
        }
    }

    private void sendMessage(String topic, String key, String value)
    {
        if (notifier != null)
        {
            try
            {
                notifier.sendMessage("property/" + topic, key, value);
            }
            catch (Throwable e)
            {
                LOGGER.warn("(FAILED) Update Notification for Topic: {}", topic, e);
            }
        }
    }
}
