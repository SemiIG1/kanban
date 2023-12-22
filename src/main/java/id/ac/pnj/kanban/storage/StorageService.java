package id.ac.pnj.kanban.storage;

import id.ac.pnj.kanban.dto.FileDTO;
import id.ac.pnj.kanban.entity.File;
import id.ac.pnj.kanban.entity.Member;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file, int projectId, Member member);

	Stream<Path> loadAll();

	Stream<FileDTO> loadAllFilesFromDatabaseByProjectId(int id);

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

	void deleteFileById(int id);

}
