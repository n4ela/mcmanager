package mcmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.ardverk.coding.BencodingInputStream;

import mcmanager.exception.CoreException;
//TODO допелить
public class TorrentInfo {

    private Set<String> fileList;

    private InputStream is;

    public TorrentInfo(File file) throws CoreException {
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new CoreException("Файл: " + file + " не найден.", e);
        }
    }

    public TorrentInfo(InputStream is) throws CoreException {
        this.is = is;
    }
    
    private Set<String> parse() throws CoreException {
        try {
        BencodingInputStream bis = new BencodingInputStream(is);
            TreeMap map = null;
            try {
                map = (TreeMap) bis.readObject();
            } catch (IOException e) {
                throw new CoreException("Ошибка при разборе torrent файла: ", e);
            }
            return parseFileList((TreeMap)map.get("info"));

        } finally {
            CloseUtils.softClose(is);
        }
    }

    private Set<String> parseFileList(TreeMap info) {
        Set<String> setFileLise = new HashSet<String>();
        String root = new String((byte[])info.get("name"));
        Object files = info.get("files");
        if (files != null) {
            for (Object fileObject : (ArrayList)files) {
                String filePath = root;
                for (Object object : (ArrayList)((TreeMap)fileObject).get("path")) {
                    filePath += "/" + new String((byte[])object);
                }
                setFileLise.add(filePath);
            } 
        } else {
            setFileLise.add(root);
        }
        return setFileLise;
    }

    public Set<String> getInfo() throws CoreException {
        if (fileList == null) {
            fileList = parse();
        }
        return fileList;
    }
}
