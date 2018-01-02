package com.incadencecorp.coalesce.services.common.jaxrs;

import com.incadencecorp.coalesce.services.common.controllers.PropertyController;

import javax.ws.rs.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * JaxRS annotations used on {@link PropertyController}
 *
 * @author Derek Clemenzi
 */
@Path("property")
interface IPropertyControllerJaxRS {

    @GET
    @Path("/{name}")
    @Produces("application/text")
    String getProperty(@PathParam("name") String name) throws RemoteException;

    @PUT
    @Path("/{name}")
    @Consumes("application/text")
    void setProperty(@PathParam("name") String name, String value) throws RemoteException;

    @GET
    @Path("/")
    @Produces("application/json")
    Map<String, String> getProperties() throws RemoteException;

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    Map<String, String> getProperties(String[] names) throws RemoteException;

    @PUT
    @Path("/")
    @Consumes("application/json")
    void setProperties(Map<String, String> values) throws RemoteException;

}