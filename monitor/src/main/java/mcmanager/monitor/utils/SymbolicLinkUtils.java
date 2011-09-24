package mcmanager.monitor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import mcmanager.exception.CoreException;

public class SymbolicLinkUtils {

    public static void createSymbolicLink(File fileLink, File targetSource) throws CoreException {
        Path link = fileLink.toPath();
        Path target = targetSource.toPath();
        try {
            Files.createSymbolicLink(link, target);
        } catch (IOException e) {
            throw new CoreException("Ошибка при создание символической ссылки: " + link, e);
        }
    }
}
