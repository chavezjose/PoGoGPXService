package pogoGPX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PoGoGPXController {

	@Autowired
    private GPXGenerationService gpxGenerationService;
    
	@Value("${file.path}")
	private String filePath;
	
    @RequestMapping("/test")
    public String greeting() {
        return "Hello World!";
    }
    
    @RequestMapping(
    	method = RequestMethod.GET,
    	value = "/gpx/quests/{pokedexNumber}/",
    	produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody Object quests(@PathVariable String pokedexNumber) throws IOException {
    	    	
    	String filename = gpxGenerationService.generateGPXQuest(pokedexNumber);
    	return returnFile(filename);

    }
    
    /** Returns the generate GPX File.
     * @throws IOException 
     * @returns response object that is a GPX file.
     */
    public Object returnFile(String filename) throws IOException {
    	    	
    	File file2Upload = new File(filePath + filename);
    	Path path = Paths.get(file2Upload.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=track.gpx");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        
        return ResponseEntity.ok()
        		.headers(header)
                .contentLength(file2Upload.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}