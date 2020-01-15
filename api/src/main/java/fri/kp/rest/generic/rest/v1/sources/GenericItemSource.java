package fri.kp.rest.generic.rest.v1.sources;

import com.kumuluz.ee.rest.beans.QueryParameters;
import fri.kp.rest.generic.entities.GenericItem;
import fri.kp.rest.generic.services.GenericItemBean;
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

@Path("/items/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
public class GenericItemSource {
    @Inject
    private GenericItemBean itemBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Returns list of all items.", summary = "Items list", tags = "items", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of items",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenericItem.class))),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = "integer"))}
            )})
    @SecurityRequirement(name = "openid-connect")
    @GET
    public Response returnAllItems(){
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<GenericItem> items = itemBean.getAll(query);
        Long allItems = itemBean.getItemListCount(query);
        return Response.status(Response.Status.OK).entity(items).header("X-Total-Count", allItems).build();
    }

    @Operation(description = "Returns an item.", summary = "Item get", tags = "items", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Single item",
                    content = @Content(schema = @Schema(implementation = GenericItem.class))
            )})
    @SecurityRequirement(name = "openid-connect")
    @GET
    @Path("{id}")
    public Response returnItem(@PathParam("id") int itemId){

        GenericItem item = itemBean.getItemById(itemId);

        if(item==null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(item).build();
    }

    @Operation(description = "Update an item.", summary = "Item update", tags = "items", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Updated an item",
                    content = @Content(schema = @Schema(implementation = GenericItem.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Item not found",
                    content = @Content(schema = @Schema(implementation = GenericItem.class))
            )
    })
    @PUT
    @Path("{id}")
    public Response updateItem(@PathParam("id") Integer id, GenericItem item) {
        return itemBean.updateItem(item, id) != null
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Delete an item.", summary = "Item delete", tags = "items", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Deleted an item",
                    content = @Content(schema = @Schema(type = "boolean"))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Item not found",
                    content = @Content(schema = @Schema(type = "boolean"))
            )
    })
    @DELETE
    @Path("{id}")
    public Response deleteItem(@PathParam("id") Integer id){
        return itemBean.deleteItem(id)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
