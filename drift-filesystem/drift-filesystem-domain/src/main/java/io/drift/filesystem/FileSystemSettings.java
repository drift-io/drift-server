package io.drift.filesystem;

import io.drift.core.recording.SubSystemDescription;
import io.drift.core.system.SubSystemConnectionDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileSystemSettings implements SubSystemConnectionDetails, SubSystemDescription, Serializable {

    private List<DirectorySettings> directories = new ArrayList<>();

    public FileSystemSettings() {}

    public void addDirectory(DirectorySettings directory) {
        directories.add(directory);
    }

    public List<DirectorySettings> getDirectories() {
        return directories;
    }
}
