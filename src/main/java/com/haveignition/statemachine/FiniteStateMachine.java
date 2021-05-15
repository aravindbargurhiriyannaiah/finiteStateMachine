package com.haveignition.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.util.annotation.NonNull;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FiniteStateMachine {
    private String currentState = null;
    private static final Logger log = LoggerFactory.getLogger(FiniteStateMachine.class);
    private final Map<String, Map<String, String>> transitionMap = new HashMap<>();

    public FiniteStateMachine(@NonNull File configFile, @NonNull String currentState) throws Exception {
        initialize(configFile, currentState);
    }

    public String next(String event) {
        this.currentState = transitionMap.get(this.currentState).get(event);
        return currentState;
    }

    private void initialize(File configFile, String currentState) throws Exception {
        if (Objects.isNull(configFile) || !StringUtils.hasText(currentState)) {
            throw new Exception("Could not create state machine. Either the config file is null or the current state is null/empty.");
        }
        List<String> strings = Files.readAllLines(configFile.toPath());
        if (CollectionUtils.isEmpty(strings)) {
            throw new Exception("Could not create state machine with empty config file");
        }
        this.currentState = currentState;
        List<String> events = Arrays.stream(strings.get(0).split(",")).collect(Collectors.toList());
        for (int stateCount = 1; stateCount < strings.size(); stateCount++) {
            int eventCount = 1;
            while (eventCount < events.size()) {
                String stateToStates = strings.get(stateCount);
                List<String> states = Arrays.stream(stateToStates.split(",")).collect(Collectors.toList());
                add(states.get(0).trim(), events.get(eventCount).trim(), states.get(eventCount).trim());
                eventCount++;
            }
        }
    }

    private void add(String currentState, String event, String nextState) {
        Map<String, String> map = transitionMap.get(currentState);
        if (CollectionUtils.isEmpty(map)) {
            map = new HashMap<>();
            transitionMap.put(currentState, map);
        }
        map.put(event, nextState);
        log.info("Added currentState: {}, event: {}, nextState: {}\n", currentState, event, nextState);
    }
}
