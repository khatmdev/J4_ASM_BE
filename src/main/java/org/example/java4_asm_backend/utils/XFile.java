package org.example.java4_asm_backend.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class XFile {
    private static final String RESOURCE_DIR = "my-resource";

    // Đọc toàn bộ nội dung file
    public static List<String> readFile(String filename) {
        try {
            Path filePath = Paths.get(RESOURCE_DIR, filename);
            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("File not found: " + filePath.toAbsolutePath());
            }
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tạo file mới nếu chưa tồn tại
    public static boolean createFileIfNotExists(String filename) {
        try {
            Path resourceDir = Paths.get(RESOURCE_DIR);
            if (!Files.exists(resourceDir)) {
                Files.createDirectories(resourceDir); // Tạo thư mục nếu chưa tồn tại
            }

            Path filePath = resourceDir.resolve(filename);
            if (Files.exists(filePath)) {
                System.out.println("File đã tồn tại: " + filePath.toAbsolutePath());
                return false; // Không làm gì nếu file đã tồn tại
            }

            Files.createFile(filePath); // Tạo file mới
            System.out.println("File được tạo mới: " + filePath.toAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa đổi nội dung file hiện tại
    public static boolean modifyFile(String filename, List<String> newContent) {
        try {
            Path filePath = Paths.get(RESOURCE_DIR, filename);
            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("File not found: " + filePath.toAbsolutePath());
            }
            Files.write(filePath, newContent); // Ghi nội dung mới vào file
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Test các phương thức
        String filename = "sample.txt";

        // 1. Tạo file nếu chưa tồn tại
        boolean isFileCreated = createFileIfNotExists(filename);
        System.out.println(isFileCreated ? "File mới được tạo." : "File đã tồn tại.");

        // 2. Sửa file (nếu cần)
        boolean isFileModified = modifyFile(filename, List.of("Dòng 1", "Dòng 2", "Dòng 3"));
        System.out.println(isFileModified ? "File đã được sửa." : "Không thể sửa file.");

        // 3. Đọc file
        List<String> fileContent = readFile(filename);
        if (fileContent != null) {
            System.out.println("Nội dung file:");
            fileContent.forEach(System.out::println);
        }
    }
}
