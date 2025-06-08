package fiap.tds.client;

import fiap.tds.dtos.NominatimSearchResponseDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey="nominatim-api")
@Produces(MediaType.APPLICATION_JSON)
public interface NominatimClient {
    @GET
    @Path("/search")
    List<NominatimSearchResponseDTO> searchByQuery(
            @QueryParam("format") String format,
            @QueryParam("q") String query,
            @QueryParam("countrycodes") String country,
            @QueryParam("limit") int limit,
            @QueryParam("accept-language") String language,
            @HeaderParam("User-Agent") String userAgent
    );
}
