package io.drift.core.recording;

import java.util.ArrayList;
import java.util.List;

public class RecordingStep {

    private String title;

    private String description;

    private List<SystemInteraction> systemInteractions = new ArrayList<>();

    public List<SystemInteraction> getSystemInteractions() {
        return systemInteractions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
