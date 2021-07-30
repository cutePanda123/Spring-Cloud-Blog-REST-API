package Controller;

import com.panda.json.result.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/healthcheck")
public class HealthCheckController {
    @GetMapping("/")
    public Object healthcheck() {
        return ResponseResult.ok();
    }
}
