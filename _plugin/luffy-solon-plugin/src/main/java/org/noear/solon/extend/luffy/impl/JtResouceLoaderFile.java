package org.noear.solon.extend.luffy.impl;

import org.noear.luffy.model.AFileModel;
import org.noear.solon.Utils;
import org.noear.solon.core.event.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @author noear
 * @since 1.3
 */
public class JtResouceLoaderFile implements JtResouceLoader {
    private String _baseUri = "/luffy/";
    private File _baseDir;

    public JtResouceLoaderFile() {
        String dirroot = Utils.getResource("/").toString().replace("target/classes/", "");

        if (dirroot.startsWith("file:")) {
            String dir_str = dirroot + "src/main/resources" + _baseUri;
            _baseDir = new File(URI.create(dir_str));
            if (!_baseDir.exists()) {
                dir_str = dirroot + "src/main/webapp" + _baseUri;
                _baseDir = new File(URI.create(dir_str));
            }
        }
    }

    @Override
    public AFileModel fileGet(String path) throws Exception {
        AFileModel file = file = new AFileModel();

        file.content = fileContentGet(path);
        if (file.content != null) {
            //如果有找到文件内容，则完善信息
            //
            File file1 = new File(path);
            String fileName = file1.getName();

            file.path = path;
            file.tag = "luffy";

            if (fileName.indexOf('.') > 0) {
                String suffix = fileName.substring(fileName.indexOf('.') + 1);
                file.edit_mode = JtMapping.getActuator(suffix);
            } else {
                file.edit_mode = JtMapping.getActuator("");
            }
        }

        return file;
    }

    protected String fileContentGet(String path) {
        if (_baseDir == null) {
            return null;
        } else {
            File file = new File(_baseDir, path);

            if (file.exists()) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    Utils.transfer(new FileInputStream(file), outputStream);
                    return outputStream.toString();
                } catch (IOException ex) {
                    EventBus.push(ex);
                    return null;
                }
            } else {
                return null;
            }
        }
    }
}
