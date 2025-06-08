package fiap.tds.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


import java.util.HashMap;
import java.util.Map;

@Path("/chat")
public class ChatResource {
    @ConfigProperty(name = "huggingface.token")
    String huggingFaceToken;

    private static final String HUGGING_FACE_API_URL = "https://api-inference.huggingface.co/models/HuggingFaceH4/zephyr-7b-beta";


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Interage com o chatbot", description = "Envia uma mensagem para o chatbot e recebe uma resposta")
    @APIResponses(value = {
            @APIResponse(responseCode = "201" , description = "Resposta do chatbot recebida com sucesso!"),
            @APIResponse(responseCode = "400", description = "Erro ao enviar mensagem para o chatbot"),
    })
    public Response talkToChatBot(Map<String, String> userEntry) {
        String message = userEntry.get("message");

        var client = ClientBuilder.newClient();
        var target = client.target(HUGGING_FACE_API_URL);

        Invocation.Builder builder = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + huggingFaceToken);

        Map<String, Object> body = new HashMap<>();
        String prompt = """
            Você é um assistente empático, que ajuda pessoas afetadas por desastres naturais.
            Responda de forma calma, curta e clara e fale em português atual, 
            dando suporte psicológico e dicas práticas que ajude o individuo.
            Usuário: %s
            Assistente:
            """.formatted(message);

        body.put("inputs", prompt);

        Map<String, Object> params = new HashMap<>();
        params.put("max_new_tokens", 300);
        params.put("temperature", 0.7);
        body.put("parameters", params);

        Response apiResponse = builder.post(Entity.json(body));
        String respostaIA = apiResponse.readEntity(String.class);
        client.close();

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(respostaIA);
            String textoGerado = root.get(0).get("generated_text").asText();

            // Pega só o que vem após "Assistente:"
            int start = textoGerado.indexOf("Assistente:");
            if (start != -1) {
                textoGerado = textoGerado.substring(start + "Assistente:".length()).trim();

                // Corta caso tenha "Usuário:" depois
                int end = textoGerado.indexOf("Usuário:");
                if (end != -1) {
                    textoGerado = textoGerado.substring(0, end).trim();
                }
            }

            Map<String, String> respostaParaFront = new HashMap<>();
            respostaParaFront.put("answer", textoGerado);

            return Response.ok(respostaParaFront).build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da IA", e);
        }
    }
}
