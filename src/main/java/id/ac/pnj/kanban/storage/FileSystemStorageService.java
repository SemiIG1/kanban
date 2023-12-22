package id.ac.pnj.kanban.storage;

import id.ac.pnj.kanban.dao.KanbanDao;
import id.ac.pnj.kanban.dto.FileDTO;
import id.ac.pnj.kanban.entity.File;
import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
	private KanbanDao kanbanDao;

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(KanbanDao kanbanDao, StorageProperties properties) {
        
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }
		this.kanbanDao = kanbanDao;
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(MultipartFile file, int projectId, Member member) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			System.out.println(destinationFile);
			double fileSize = 0;

			System.out.println("File size: " + fileSize);

			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
			File fileEntity = new File(destinationFile.toString(), LocalDateTime.now());
			fileEntity.setSize((int) file.getSize());
			Project projectEntity = kanbanDao.findProjectById(projectId);
			fileEntity.setProject(projectEntity);
			fileEntity.setMember(member);
			kanbanDao.save(fileEntity);
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {

		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Stream<FileDTO> loadAllFilesFromDatabaseByProjectId(int id) {
		List<File> files = kanbanDao.findAllFilesByProjectId(id);
		Stream<FileDTO> fileDTOList = files.stream().map(
				file -> {
					if (Files.exists(Paths.get(file.getPath()))) {
						System.out.println("File in " + file.getPath() + " exists");
					}

					FileDTO fileDTO = new FileDTO();
					fileDTO.setId(file.getId());

					fileDTO.setPath(file.getPath());
					fileDTO.setCreatedAt(file.getCreatedAt());
					fileDTO.setCreatedBy(file.getMember().getName());
					StringBuilder stringBuilder = new StringBuilder();
					DecimalFormat decimalFormat = new DecimalFormat("#.#");
					double fileSize = 0;
					if (file.getSize() > 1048576) {
						fileSize = (double) file.getSize() / 1048576;

						stringBuilder.append(decimalFormat.format(fileSize));
						stringBuilder.append(" MB");
					} else if (file.getSize() > 1024) {
						fileSize = (double) file.getSize() / 1024;
						stringBuilder.append(decimalFormat.format(fileSize));
						stringBuilder.append(" KB");
					} else {
						fileSize = file.getSize();
						stringBuilder.append(fileSize);
						stringBuilder.append(" bytes");
					}
					fileDTO.setSize(stringBuilder.toString());
					return fileDTO;
				}
		);
		return fileDTOList;
	}


	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {

		FileSystemUtils.deleteRecursively(rootLocation.toFile());

	}

	@Override
	public void deleteFileById(int id) {
		File file = kanbanDao.findFileById(id);
		try {
			Files.deleteIfExists(Paths.get(file.getPath()));
		} catch (Exception e) {
			throw new StorageFileNotFoundException("Could not delete file");
		}
		kanbanDao.deleteFileById(id);
	}


	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
