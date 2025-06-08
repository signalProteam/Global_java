package fiap.tds.services;

import fiap.tds.client.NominatimClient;
import fiap.tds.dtos.HelpRequestDTO;
import fiap.tds.dtos.NominatimSearchResponseDTO;
import fiap.tds.models.HelpRequest;
import fiap.tds.models.Status;
import fiap.tds.repositories.HelpRequestRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;


@ApplicationScoped
public class HelpRequestService {

    @Inject
    HelpRequestRepository helpRequestRepository;

    @Inject
    @RestClient
    NominatimClient nominatimClient;

    private final String appUserAgent = "MeuAppSOS-FIAP/1.0 (rm561160@fiap.com.br)";
    private static final Long SYSTEM_USER_ID = 1L;

    // This another way to implement the method, returns the entity created, and show the id etc.
    @Transactional
    public HelpRequest reportHelpRequest(HelpRequestDTO helpDto) {
        var help = new HelpRequest();
        help.setNotes(helpDto.getNotes());
        help.setContactInfo(helpDto.getContactInfo());
        help.setRequestTimestamp(LocalDateTime.now());
        help.setStatus(Status.PENDENTE);

        try {
            List<NominatimSearchResponseDTO> results = nominatimClient.searchByQuery(
                    "json",
                    helpDto.getCep(),
                    "br",
                    1,
                    "pt-BR",
                    appUserAgent
            );

            if (results != null && !results.isEmpty()) {
                var location = results.get(0);

                help.setLatitude(Double.parseDouble(location.lat));
                help.setLongitude(Double.parseDouble(location.lon));
                help.setEnderecoAproximado(location.display_name);
                help.setCep(helpDto.getCep());
                help.setUserId(SYSTEM_USER_ID);
                help.setUpdateBy(SYSTEM_USER_ID);
            } else {
                System.err.println("Nominatim: Nenhuma coordenada encontrada para o CEP fornecido.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar localização via CEP: " + e.getMessage());
        }

        helpRequestRepository.save(help);
        return help;
    }


    public void resolveHelpRequest(Long id) {
        var help = helpRequestRepository.findById(id);
        if (help.isEmpty() || help.get().getStatus().equals(Status.CONCLUIDO)) {
            throw new NotFoundException("Solicitação de ajuda não encontrada ou já concluída.");
        }
        var helpRequest = help.get();
        if (helpRequest.getStatus().equals(Status.PENDENTE)) {
            helpRequest.setStatus(Status.EM_ANDAMENTO);
        } else if (helpRequest.getStatus().equals(Status.EM_ANDAMENTO)) {
            helpRequest.setStatus(Status.CONCLUIDO);
        }
        helpRequestRepository.save(helpRequest);
    }


    public HelpRequest findHelpById(Long id) {
        return helpRequestRepository.findById(id).orElseThrow(() -> new fiap.tds.exceptions.NotFoundException("Pedido de ajuda com ID " + id + " não encontrado."));
    }

    // This method will return all help requests, GET request maybe will exclusively for admins.
    public List<HelpRequest> getAllHelpRequests() {
        var helpRequests = helpRequestRepository.findAll();
        if (!helpRequests.isEmpty()) {
            return helpRequests.stream()
                    .filter(h -> h.getStatus().equals(Status.PENDENTE) || h.getStatus().equals(Status.EM_ANDAMENTO))
                    .toList();
        } else {
            throw new fiap.tds.exceptions.NotFoundException("Nenhuma solicitação de ajuda encontrada.");
        }
    }

}



