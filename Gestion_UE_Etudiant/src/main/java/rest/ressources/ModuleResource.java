package rest.ressources;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/modules")
public class ModuleResource {

    private ModuleBusiness moduleBusiness = new ModuleBusiness();
    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    // 1. Création d’un nouveau Module
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModule(Module module) {
        if (moduleBusiness.addModule(module)) {
            return Response.status(Response.Status.CREATED).entity(module).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 2. Liste de tous les modules
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModules() {
        return Response.ok(moduleBusiness.getAllModules()).build();
    }

    // 3. Récupération d’un module par matricule
    @GET
    @Path("/{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModule(@PathParam("matricule") String matricule) {
        Module m = moduleBusiness.getModuleByMatricule(matricule);
        if (m != null) {
            return Response.ok(m).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 4. Suppression d’un module
    @DELETE
    @Path("/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        if (moduleBusiness.deleteModule(matricule)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 5. Modification d’un module
    @PUT
    @Path("/{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateModule(@PathParam("matricule") String matricule, Module module) {
        if (moduleBusiness.updateModule(matricule, module)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 6. Récupérer les modules d’une UE (par codeUE)
    @GET
    @Path("/UE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulesByUE(@QueryParam("codeUE") int codeUE) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        if (ue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Module> liste = moduleBusiness.getModulesByUE(ue);
        return Response.ok(liste).build();
    }
}