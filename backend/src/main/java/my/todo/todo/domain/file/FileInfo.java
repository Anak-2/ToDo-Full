package my.todo.todo.domain.file;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Deprecated
@Entity
@Getter
@NoArgsConstructor
public class FileInfo {
    @Id @GeneratedValue
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;

    @Builder
    public FileInfo(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
