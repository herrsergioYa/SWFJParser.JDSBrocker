/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialization;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HerrSergio
 */
public class IOHelper {

    public static byte[] toBytes(Serializable object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            serialize(object, bos);
            return bos.toByteArray();
        }
    }

    public static Object fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return deserialize(bis);
        }
    }

    public static void serialize(Object object, OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(object);
        oos.flush();
    }

    public static Object deserialize(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }

    public static <E extends Serializable> E clone(E e) {
        try {
            return (E) fromBytes(toBytes(e));
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void serialize(Object object, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            serialize(object, fos);
        }
    }

    public static void serialize(Object object, String file) throws IOException {
        serialize(object, new File(file));
    }

    public static void serialize(Object object, Path path) throws IOException {
        serialize(object, path.toFile());
    }

    public static Object deserialize(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return deserialize(fis);
        }
    }

    public static Object deserialize(String file) throws IOException, ClassNotFoundException {
        return deserialize(new File(file));
    }

    public static Object deserialize(Path path) throws IOException, ClassNotFoundException {
        return deserialize(path.toFile());
    }

    public static void write(CharSequence text, Path path, Charset cs) throws IOException {
        write(Collections.nCopies(1, text), path, cs);
    }

    public static void write(Iterable<? extends CharSequence> text, Path path, Charset cs) throws IOException {
        Files.write(path, text, cs);
    }

    public static List<String> read(Path path, Charset cs) throws IOException {
        return Files.readAllLines(path, cs);
    }

    public static void writeBytes(byte[] bytes, Path path) throws IOException {
        Files.write(path, bytes);
    }

    public static byte[] readBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public static void write(CharSequence text, String path, Charset cs) throws IOException {
        write(text, Paths.get(path), cs);
    }

    public static void write(Iterable<? extends CharSequence> text, String path, Charset cs) throws IOException {
        write(text, Paths.get(path), cs);
    }

    public static List<String> read(String path, Charset cs) throws IOException {
        return read(Paths.get(path), cs);
    }

    public static void writeBytes(byte[] bytes, String path) throws IOException {
        writeBytes(bytes, Paths.get(path));
    }

    public static byte[] readBytes(String path) throws IOException {
        return readBytes(Paths.get(path));
    }

    public static void write(CharSequence text, File path, Charset cs) throws IOException {
        write(text, path.toPath(), cs);
    }

    public static void write(Iterable<? extends CharSequence> text, File path, Charset cs) throws IOException {
        write(text, path.toPath(), cs);
    }

    public static List<String> read(File path, Charset cs) throws IOException {
        return read(path.toPath(), cs);
    }

    public static void writeBytes(byte[] bytes, File path) throws IOException {
        writeBytes(bytes, path.toPath());
    }

    public static byte[] readBytes(File path) throws IOException {
        return readBytes(path.toPath());
    }

    public static void write(CharSequence text, Path path) throws IOException {
        write(text, path, StandardCharsets.UTF_8);
    }

    public static void write(Iterable<? extends CharSequence> text, Path path) throws IOException {
        write(text, path, StandardCharsets.UTF_8);
    }

    public static List<String> read(Path path) throws IOException {
        return read(path, StandardCharsets.UTF_8);
    }

    public static void write(CharSequence text, String path) throws IOException {
        write(text, path, StandardCharsets.UTF_8);
    }

    public static void write(Iterable<? extends CharSequence> text, String path) throws IOException {
        write(text, path, StandardCharsets.UTF_8);
    }

    public static List<String> read(String path) throws IOException {
        return read(path, StandardCharsets.UTF_8);
    }

    public static void write(CharSequence text, File path) throws IOException {
        write(text, path, StandardCharsets.UTF_8);
    }

    public static void write(Iterable<? extends CharSequence> text, File path) throws IOException {
        write(text, path, StandardCharsets.UTF_8);
    }

    public static List<String> read(File path) throws IOException {
        return read(path, StandardCharsets.UTF_8);
    }

    public static Object deserialize(URL file) throws IOException, ClassNotFoundException {
        try (InputStream is = file.openStream()) {
            return deserialize(is);
        }
    }

    public static List<String> read(URL path, Charset cs) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(path.openStream(), cs))) {
            while (true) {
                String buf = reader.readLine();
                if (buf == null) {
                    break;
                }
                list.add(buf);
            }
        }
        return list;
    }
    
    public static List<String> read(URL path) throws IOException {
        return read(path, StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(URL path) throws IOException {
        byte[] buffer = new byte[1 << 10];
        int pos = 0;
        try (InputStream reader = new BufferedInputStream(path.openStream())) {
            while (true) {
                int count = reader.read(buffer, pos, buffer.length - pos);
                if(count < 0)
                    break;
                pos += count;
                if(pos == buffer.length)
                    buffer = Arrays.copyOf(buffer, buffer.length << 1);
            }
        }
        return buffer.length != pos ? Arrays.copyOf(buffer, pos) : buffer;
    }
}
