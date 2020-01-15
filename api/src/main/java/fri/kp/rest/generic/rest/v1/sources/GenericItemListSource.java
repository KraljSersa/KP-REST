package fri.kp.rest.generic.rest.v1.sources;


import com.kumuluz.ee.rest.beans.QueryParameters;
import fri.kp.rest.generic.entities.GenericItem;
import fri.kp.rest.generic.entities.GenericItemList;
import fri.kp.rest.generic.services.GenericItemBean;
import fri.kp.rest.generic.services.GenericItemListBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/itemList/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
public class GenericItemListSource {

    @Inject
    private GenericItemListBean slb;

    @Inject
    private GenericItemBean itemBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Returns list of all generic lists.", summary = "genericList list", tags = "genericLists", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of generic lists",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenericItemList.class))),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = "integer"))}
            )})
    @SecurityRequirement(name = "openid-connect")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnAllGenericLists(){
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<GenericItemList> users = slb.getGenericLists(query);
        Long genericListCount = slb.getGenericListCount(query);
        return Response.status(Response.Status.OK).entity(users).header("X-Total-Count", genericListCount).build();
    }

    @Operation(description = "Returns a generic list.", summary = "genericList get", tags = "genericLists", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Single generic list",
                    content = @Content(schema = @Schema(implementation = GenericItemList.class))
            )})
    @SecurityRequirement(name = "openid-connect")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnGenericList(@PathParam("id") Integer id){
        GenericItemList list;
        list = slb.getGenericListById(id);
        if(list == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return  Response.status(Response.Status.OK).entity(list).build();
    }

    @Operation(description = "Create a generic list.", summary = "genericList create", tags = "genericLists", responses = {
            @ApiResponse(responseCode = "201",
                    description = "Created a generic list",
                    content = @Content(schema = @Schema(implementation = GenericItemList.class))
            )})
    @SecurityRequirement(name = "openid-connect")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGenericList(){
        return Response.status(Response.Status.CREATED).entity(slb.createGenericList()).build();
    }

    @Operation(description = "Add item to generic list.", summary = "genericList add item", tags = "genericLists", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Added item to generic list",
                    content = @Content(schema = @Schema(type = "boolean"))
            )})
    @SecurityRequirement(name = "openid-connect")
    @POST
    @Path("{id}/item")
    public Response addItemToGenericList(@PathParam("id") Integer id, GenericItem item){
        return slb.addItemToGenericList(item,id)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Delete a generic list.", summary = "genericList delete", tags = "genericLists", responses = {
            @ApiResponse(responseCode = "203",
                    description = "Deleted a genericList",
                    content = @Content(schema = @Schema(type = "boolean"))
            ),
            @ApiResponse(responseCode = "404",
                    description = "genericList not found",
                    content = @Content(schema = @Schema(type = "boolean"))
            )
    })
    @DELETE
    @Path("{id}")
    public Response deleteGenericList(@PathParam("id") Integer id){
        return slb.deleteGenericList(id)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Remove item from generic list.", summary = "genericList remove item", tags = "genericLists", responses = {
            @ApiResponse(responseCode = "203",
                    description = "Removed item from genericList",
                    content = @Content(schema = @Schema(type = "boolean"))
            ),
            @ApiResponse(responseCode = "404",
                    description = "genericList not found",
                    content = @Content(schema = @Schema(type = "boolean"))
            )
    })
    @DELETE
    @Path("{id}/item/{itemid}")
    public Response deleteItemFromGenericList(@PathParam("id")Integer id , @PathParam("itemid") Integer itemid){
        GenericItem item = itemBean.getItemById(itemid);
        if(item == null) Response.status(Response.Status.NOT_FOUND).build();
        return slb.removeItemFromGenericList(item, id)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.NOT_FOUND).build();

    }
}
