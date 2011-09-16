package mcmanager.utils;

import java.io.File;

import mcmanager.exception.CoreException;

public class FileUtils {
    public static void removeFile(String fileName) throws CoreException {
        File file = new File(fileName);
        if (file.exists() && !file.delete())
            throw new CoreException("Ошибка при удаление файла: " + fileName);
    }
}
