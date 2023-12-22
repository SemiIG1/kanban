package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.dto.FileDTO;
import id.ac.pnj.kanban.entity.File;
import id.ac.pnj.kanban.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Used for prototyping Bootstrap design
// Don't use git on this class
@Controller
@RequestMapping("/")
public class PrototypingController {
    private StorageService storageService;

    public PrototypingController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/prototype")
    public String prototype(Model model) {
        Stream<FileDTO> files = storageService.loadAllFilesFromDatabaseByProjectId(1);
        List<FileDTO> fileDTOList = files.map(file -> {
            file.setPath(MvcUriComponentsBuilder.fromMethodName(
                    PrototypingController.class,"serveFile",
                    Paths.get(file.getPath()).getFileName().toString()).build().toUri().toString());
            return file;
        }).collect(Collectors.toList());
        model.addAttribute("files", fileDTOList);
        return "prototype";
    }

    @GetMapping("/prototype/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
