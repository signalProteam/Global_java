package fiap.tds.exceptions;

import fiap.tds.dtos.ErrorResponseDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ResourceNotFoundMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        int statusCode = Response.Status.NOT_FOUND.getStatusCode();

        ErrorResponseDTO erroDTO = new ErrorResponseDTO(exception.getMessage(), statusCode);


        return Response.status(statusCode).entity(erroDTO).build();
    }
}
