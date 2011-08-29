package mcmanager;

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
import mcmanager.utils.CloseUtils;
//TODO допелить
public class TorrentInfo {

    private Set<String> fileList;
    
    public TorrentInfo(File file) {
        fileList = new HashSet<String>();
    }
    
    private void parse(File file) throws CoreException {
        InputStream is = null;
        try {
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new CoreException("Файл: " + file + " не найден.", e);
            }
            BencodingInputStream bis = new BencodingInputStream(is);
            TreeMap map = null;
            try {
                map = (TreeMap) bis.readObject();
            } catch (IOException e) {
                throw new CoreException("Ошибка при разборе torrent файла: " + file, e);
            }
            fileList = parseFileList((TreeMap)map.get("info"));
            
        } finally {
            CloseUtils.softClose(is);
        }
    }
    
    private Set<String> parseFileList(TreeMap info) {
        Set<String> setFileLise = new HashSet<String>();
        for (Object fileObject : (ArrayList)info.get("files")) {
            String filePath = "";
            for (Object object : (ArrayList)((TreeMap)fileObject).get("path")) {
                filePath += "/" + new String((byte[])object);
            }
            setFileLise.add(filePath);
        }
        return setFileLise;
    }
}
