package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.restdto.response.HomeResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.BaseResponseDTO;

public interface HomeService {
    BaseResponseDTO<HomeResponseDTO> getHomeData();
}
