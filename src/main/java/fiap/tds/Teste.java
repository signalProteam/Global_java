package fiap.tds;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class Teste {
    @GET
    public String teste() {
        return "Teste";
    }
}
