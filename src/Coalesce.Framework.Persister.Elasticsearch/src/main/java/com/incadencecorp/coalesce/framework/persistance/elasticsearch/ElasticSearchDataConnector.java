package com.incadencecorp.coalesce.framework.persistance.elasticsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.net.HostAndPort;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.persistance.CoalesceDataConnectorBase;

import ironhide.client.IronhideClient;
import ironhide.client.IronhideClient.Builder;

public class ElasticSearchDataConnector extends CoalesceDataConnectorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchDataConnector.class);

    // Datastore Properties
    public static final String INSTANCE_ID = "instanceId";
	public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String ELASTICHOSTS_PROPERTY = "elastichosts";
    private static final String KEYSTORE_FILE_PROPERTY = "keystore_file";
    private static final String TRUSTSTORE_FILE_PROPERTY = "truststore_file";

    public static String getInstanceId() {
		return INSTANCE_ID;
	}

	public TransportClient getDBConnector() throws CoalescePersistorException {
		TransportClient client = null;
		// on startup
		//String keypath = props.getProperty(KEYSTORE_FILE_PROPERTY);
		//String trustpath = props.getProperty(TRUSTSTORE_FILE_PROPERTY);

		try {
			Settings settings = Settings.builder()
				    .put("cluster.name", "elasticsearch")
		            .build();

			client = new PreBuiltTransportClient(settings);

			/*Settings settings = ImmutableSettings.settingsBuilder()
		    .put("cluster.name", "RDK.bdpdev.incadencecorp.com")
	        .put("plugins." + PluginsService.LOAD_PLUGIN_FROM_CLASSPATH, false)
	        .put("plugin.types", IronhideClientPlugin.class)
	        .put(ironhide.transport.ConfigConstants.sslSettings(
	        		keypath,
	                "changeit",
	                trustpath,
	                "changeit"
	                ))
            .build();	
			
			client = new TransportClient(
					settings);
					*/

			String eshosts = "localhost:9300";
			Stream.of(eshosts.split(","))
	        .map(host -> {
	            HostAndPort hostAndPort = HostAndPort
	                    .fromString(host)
	                    .withDefaultPort(9300);

	            try {
					return new InetSocketTransportAddress(InetAddress.getByName(hostAndPort.getHostText()), hostAndPort.getPort());
				} catch (UnknownHostException e) {
					LOGGER.warn(e.getMessage(), e);
					return null;
				}
	        })
	        .forEach(client::addTransportAddress);

//			        .addTransportAddress(new InetSocketTransportAddress("bdpnode3", 9300))
//			        .addTransportAddress(new InetSocketTransportAddress("bdpnode4", 9300));
//			        .addTransportAddress(new InetSocketTransportAddress("bdpnode5", 9300));

		} catch (ElasticsearchException ex) {
			throw new CoalescePersistorException(ex.getMessage(), ex);
		}

		return client;
	}

	public Connection getDBConnection() throws SQLException {
		LOGGER.error("ElasticSearchDataConnector:getDBConnection: Procedure not implemented");
		throw new UnsupportedOperationException("ElasticSearchDataConnector:getDBConnection: Procedure not implemented");
	}

    @Override
    protected String getProcedurePrefix()
    {
        return "";
    }
    
    public IronhideClient connectElasticSearch(Properties props)
    {

        IronhideClient client = null;
        // on startup
        String keypath = props.getProperty(KEYSTORE_FILE_PROPERTY);
        String trustpath = props.getProperty(TRUSTSTORE_FILE_PROPERTY);

        try
        {

            Builder clientBuild = IronhideClient.builder().setClusterName("BDP.oerepodev.incadencecorp.com")
            		.clientSSLSettings(keypath, "changeit",
            				trustpath, "changeit");

            String eshosts = props.getProperty(ELASTICHOSTS_PROPERTY);
            Stream.of(eshosts.split(",")).map(host -> {
                HostAndPort hostAndPort = HostAndPort.fromString(host).withDefaultPort(9300);

                try
                {
                    String chost = hostAndPort.getHostText();
                    InetAddress addr = InetAddress.getByName(chost);
                    return new InetSocketTransportAddress(addr, hostAndPort.getPort());
                }
                catch (UnknownHostException ex)
                {
                    // TODO Auto-generated catch block
                    LOGGER.error(ex.getMessage(), ex);
                    return null;
                }

            }).forEach(clientBuild::addTransportAddress);
            client = clientBuild.build();

            //			        .addTransportAddress(new InetSocketTransportAddress("bdpnode3", 9300))
            //			        .addTransportAddress(new InetSocketTransportAddress("bdpnode4", 9300));
            //			        .addTransportAddress(new InetSocketTransportAddress("bdpnode5", 9300));

        }
        catch (ElasticsearchException | FileNotFoundException ex)
        {
            // TODO Auto-generated catch block
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }

        return client;
    }

    public static void main(String args[]) {
    	ElasticSearchDataConnector connector;
		try {
			connector = new ElasticSearchDataConnector();
			TransportClient client = connector.getDBConnector();

			System.out.println("Client connected");


	        //TestEntity entity = new TestEntity();
	        //entity.initialize();

	        ObjectMapper mapper = new ObjectMapper();
	        mapper.enable(SerializationFeature.INDENT_OUTPUT);
	        mapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);

			try {
				IndexResponse response = client.prepareIndex("twitter4", "tweet", "1")
				        .setSource(jsonBuilder()
				                .startObject()
				                    .field("user", "kimchyDude")
				                    .field("postDate", new Date())
				                    .field("message", "trying out Elasticsearch")
				                .endObject()
				              )
				    .get();

		        //String result = mapper.writerWithView(Views.Public.class).writeValueAsString(entity);

		        //System.out.println(result);

				// Index name
				String _index = response.getIndex();
				// Type name
				String _type = response.getType();
				// Document ID (generated or not)
				String _id = response.getId();
				// Version (if it's the first time you index this document, you will get: 1)
				long _version = response.getVersion();
				// status has stored current instance statement.
				//RestStatus status = response.;
				System.out.println(_index + ", " + _type + ", " + _id + ", " + _version);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (CoalescePersistorException e) {
			e.printStackTrace();
		}
    }



}
