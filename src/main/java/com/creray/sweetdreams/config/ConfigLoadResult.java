package com.creray.sweetdreams.config;

import lombok.Getter;

@Getter
public class ConfigLoadResult {
    private final Config config;
    private final Exception exception;
    private final boolean successLoaded;

    private ConfigLoadResult(Config config) {
        this.config = config;
        this.exception = null;
        successLoaded = true;
    }

    private ConfigLoadResult(Config config, Exception exception) {
        this.config = config;
        this.exception = exception;
        successLoaded = false;
    }

    public static ConfigLoadResult success(Config config) {
        return new ConfigLoadResult(config);
    }

    public static ConfigLoadResult exceptionally(Config config, Exception exception) {
        return new ConfigLoadResult(config, exception);
    }
}
