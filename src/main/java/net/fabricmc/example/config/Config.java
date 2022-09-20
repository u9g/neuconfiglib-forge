package net.fabricmc.example.config;

public interface Config {
    void executeRunnable(String runnableId);
    String getHeaderText();
}
