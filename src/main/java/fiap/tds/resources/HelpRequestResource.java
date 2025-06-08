package fiap.tds.resources;

import fiap.tds.dtos.HelpRequestDTO;
import fiap.tds.services.HelpRequestService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/solicitar-ajuda")
public class HelpRequestResource {
    @Inject
    HelpRequestService helpRequestService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Reporta Solicitação de Ajuda", description = "Reportar uma solicitação de ajuda com status SEM_RESPOSTA")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Solicitação de ajuda reportada com sucesso!"),
            @APIResponse(responseCode = "400", description = "Erro ao reportar solicitação de ajuda")
    })
    public Response reportHelpRequest(HelpRequestDTO helpDto){
        var helpRequest = helpRequestService.reportHelpRequest(helpDto);
        return Response.status(Response.Status.CREATED).entity(helpRequest).build();
    }
}
