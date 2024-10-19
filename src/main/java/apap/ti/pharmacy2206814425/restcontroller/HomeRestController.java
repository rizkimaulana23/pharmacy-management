package apap.ti.pharmacy2206814425.restcontroller;

import apap.ti.pharmacy2206814425.restdto.response.HomeResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.BaseResponseDTO;
import apap.ti.pharmacy2206814425.restservice.HomeRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeRestController {

    @Autowired
    HomeRestService homeService;

    @GetMapping("")
    public ResponseEntity<?> getHome() {
        var baseResponse = new BaseResponseDTO<HomeResponseDTO>();
        HomeResponseDTO responseDTO = homeService.getHome();
        baseResponse.setResult(responseDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
