package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.service.filler.FillerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("api/filler")
public class FillerApi {

    private final FillerServiceImpl fillerService;

    @Autowired
    public FillerApi(FillerServiceImpl fillerService) {
        this.fillerService = fillerService;
    }

    @PostMapping(value = "/{pid}")
    public void enrichPublication(@PathVariable("pid") String pid) {
        fillerService.enrichPublication(pid);
//        throw new NotImplementedException("Logging to Tasks fails if called synchronously");
    }
}
