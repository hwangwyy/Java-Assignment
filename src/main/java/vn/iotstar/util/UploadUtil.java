package vn.iotstar.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/** Multipart parsing (commons-fileupload) + safe file storage under the upload directory. */
public final class UploadUtil {
    private static final Set<String> ALLOWED_EXT = Set.of("png", "jpg", "jpeg", "gif", "webp");
    private static final long MAX_FILE_BYTES = 5L * 1024 * 1024;

    private UploadUtil() {
    }

    /** Result of parsing a multipart form. */
    public static final class Result {
        public final Map<String, String> fields = new HashMap<>();
        /** Relative path of the stored file (e.g. "category/abc.png"), or null if no file was uploaded. */
        public String savedFile;
        /** Validation error message, or null. */
        public String error;

        public String field(String name) {
            String v = fields.get(name);
            return v == null ? "" : v.trim();
        }
    }

    public static Result parse(HttpServletRequest req, String subFolder, String fileField)
            throws FileUploadException, IOException {
        Result result = new Result();
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        upload.setFileSizeMax(MAX_FILE_BYTES);

        List<FileItem> items = upload.parseRequest(req);
        for (FileItem item : items) {
            if (item.isFormField()) {
                result.fields.put(item.getFieldName(), item.getString("UTF-8"));
            } else if (fileField.equals(item.getFieldName()) && item.getSize() > 0) {
                String original = FilenameUtils.getName(item.getName());
                String ext = FilenameUtils.getExtension(original).toLowerCase(Locale.ROOT);
                if (!ALLOWED_EXT.contains(ext)) {
                    result.error = "Chỉ chấp nhận ảnh: " + String.join(", ", ALLOWED_EXT);
                } else {
                    String relative = subFolder + "/" + UUID.randomUUID() + "." + ext;
                    Path target = resolve(relative);
                    Files.createDirectories(target.getParent());
                    try (InputStream in = item.getInputStream()) {
                        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                    result.savedFile = relative;
                }
            }
            item.delete();
        }
        return result;
    }

    /** Resolves a relative path inside the upload dir; returns null if it would escape that dir. */
    public static Path resolve(String relative) {
        if (relative == null || relative.isEmpty()) {
            return null;
        }
        Path root = AppConfig.uploadDir();
        Path p = root.resolve(relative).normalize();
        return p.startsWith(root) ? p : null;
    }

    public static void deleteQuietly(String relative) {
        try {
            Path p = resolve(relative);
            if (p != null) {
                Files.deleteIfExists(p);
            }
        } catch (IOException ignored) {
            // nothing to do
        }
    }
}
