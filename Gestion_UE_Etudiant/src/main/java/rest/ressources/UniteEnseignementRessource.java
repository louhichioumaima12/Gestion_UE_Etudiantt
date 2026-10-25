package rest.ressources;

import metiers.UniteEnseignementBusiness;
import entities.UniteEnseignement;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("UE")
public class UniteEnseignementRessource {

    private static final UniteEnseignementBusiness business = new UniteEnseignementBusiness();

    // POST /UE  (XML) -> 201 Created | 404 Not Found
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addUE(UniteEnseignement ue) {
        if (business.addUniteEnseignement(ue)) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // GET /UE | GET /UE?semestre=2 | GET /UE?code=123  (JSON)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUE(@QueryParam("semestre") Integer semestre,
                          @QueryParam("code") Integer code) {
        if (code != null) {
            UniteEnseignement ue = business.getUEByCode(code);
            if (ue == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(ue).build();
        }
        List<UniteEnseignement> liste = (semestre != null)
                ? business.getUEBySemestre(semestre)
                : business.getListeUE();
        return Response.ok(new GenericEntity<List<UniteEnseignement>>(liste) {}).build();
    }

    // DELETE /UE/{code} -> 200 | 404
    @DELETE
    @Path("{code}")
    public Response deleteUE(@PathParam("code") int code) {
        if (business.deleteUniteEnseignement(code)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // PUT /UE/{code} (XML) -> 200 | 404
    @PUT
    @Path("{code}")
    @Consumes(MediaType.APPLICATION_XML)
    public Response updateUE(@PathParam("code") int code, UniteEnseignement ue) {
        if (business.updateUniteEnseignement(code, ue)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}