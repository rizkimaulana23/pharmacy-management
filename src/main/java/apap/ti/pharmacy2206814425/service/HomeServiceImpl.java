package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.restdto.response.HomeResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.BaseResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HomeServiceImpl implements HomeService {

    private final WebClient webClient;

    public HomeServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://localhost:8080/api")
                .build();
    }

    @Override
    public BaseResponseDTO<HomeResponseDTO> getHomeData() {
        var response = webClient
                .get()
                .uri("http://localhost:8080/api")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<HomeResponseDTO>>() {})
                .block();

        return response;
    }
}
