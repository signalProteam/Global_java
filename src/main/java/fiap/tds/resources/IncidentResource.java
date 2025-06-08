package fiap.tds.resources;

import fiap.tds.dtos.HelpRequestDTO;
import fiap.tds.dtos.HelpRequestResponseDTO;
import fiap.tds.models.HelpRequest;
import fiap.tds.services.HelpRequestService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.stream.Collectors;

@Path("/incidentes")
public class IncidentResource {
    @Inject
    HelpRequestService helpService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Lista todos os incidentes", description = "Retorna uma lista com todos os incidentes registrados")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de incidentes retornada com sucesso!"),
            @APIResponse(responseCode = "204", description = "Nenhum incidente encontrado")
    })
    public Response getAllIncidents() {
        var incidents = helpService.getAllHelpRequests();
        if (incidents.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).entity("Nenhum incidente encontrado").build();
        }
        var incidentsFiltered = incidents.stream()
                .map(h -> new HelpRequestResponseDTO(
                        h.getId(),
                        h.getCep(),
                        h.getNotes(),
                        h.getContactInfo(),
                        h.getLatitude(),
                        h.getLongitude(),
                        h.getStatus().name()
                ))
                .collect(Collectors.toList());

        return Response.ok(incidentsFiltered).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Operation(summary = "Busca um incidente por ID", description = "Retorna os detalhes de um incidente específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Incidente encontrado com sucesso!"),
            @APIResponse(responseCode = "404", description = "Incidente não encontrado")
    })
    public Response getIncidentById(@PathParam("id") Long id) {
        return Response.ok(helpService.findHelpById(id)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Operation(summary = "Atualiza um incidente", description = "Atualiza os detalhes de um incidente existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Incidente atualizado com sucesso!"),
            @APIResponse(responseCode = "404", description = "Incidente não encontrado")
    })
    public Response resolveIncident(@PathParam("id") Long id) {
        try {
            helpService.resolveHelpRequest(id);
            return Response.ok("Incidente resolvido com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Não foi possível encontrar o incidente: " + e.getMessage()).build();
        }
    }
}
