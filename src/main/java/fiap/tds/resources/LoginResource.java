package fiap.tds.resources;

import fiap.tds.dtos.LoginDTO;
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

@Path("/login")
public class LoginResource {
    @Inject
    UsersService usersService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Realiza o login do usuário", description = "Retorna uma mensagem de sucesso ou erro")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Login realizado com sucesso!"),
            @APIResponse(responseCode = "401", description = "Credenciais inválidas!")
    })
    public Response login(LoginDTO loginDTO) {
        boolean success = usersService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if(success) {
            return  Response.status(Response.Status.OK).entity("Login realizado com sucesso!").build();
        }
        else  {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }
}
