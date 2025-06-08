package fiap.tds.resources;

import fiap.tds.dtos.UsersDTO;
import fiap.tds.models.Users;
import fiap.tds.services.UsersService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/usuarios")
public class UserRegisterResource {

    @Inject
    UsersService usersService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cadastra um novo usuário", description = "Cadastra um novo usuário")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!"),
            @APIResponse(responseCode = "400", description = "Erro ao cadastrar usuário"),

    })
    public Response register(UsersDTO usersDTO) {
        usersService.register(usersDTO);
        return Response.status(Response.Status.CREATED).entity("Usuário cadastrado com sucesso!").build();
    }
}
