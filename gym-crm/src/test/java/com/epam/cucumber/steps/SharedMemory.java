package com.epam.cucumber.steps;


import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SharedMemory {

    private final HashMap<String, Object> sharedMemory = new HashMap<>();

    public void put(String key, Object value) {
        sharedMemory.put(key, value);
    }

    public Object get(String key) {
        return sharedMemory.get(key);
    }
}
