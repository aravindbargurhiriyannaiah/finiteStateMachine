package com.haveignition.statemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

@ExtendWith(SpringExtension.class)
class FiniteStateMachineTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testFSM_ValidConfigValidCurrentState_symmetric() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:symmetricStateMachine.csv");
        File configFile = resource.getFile();
        FiniteStateMachine fsm = new FiniteStateMachine(configFile, "state1");
        String nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state3", nextState);

        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state3", nextState);

        nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state3", nextState);
    }

    @Test
    public void testFSM_ValidConfigValidCurrentState_asymmetric_1() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:moreEventsThanStatesStateMachine.csv");
        File configFile = resource.getFile();
        FiniteStateMachine fsm = new FiniteStateMachine(configFile, "state1");
        String nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state3", nextState);
        nextState = fsm.next("event4");
        Assertions.assertEquals("state3", nextState);
    }

    @Test
    public void testFSM_ValidConfigValidCurrentState_asymmetric_2() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:moreStatesThanEventsStateMachine.csv");
        File configFile = resource.getFile();
        FiniteStateMachine fsm = new FiniteStateMachine(configFile, "state1");
        String nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state4", nextState);

        nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state4", nextState);

        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);

        nextState = fsm.next("event3");
        Assertions.assertEquals("state4", nextState);
        nextState = fsm.next("event1");
        Assertions.assertEquals("state1", nextState);
        nextState = fsm.next("event2");
        Assertions.assertEquals("state2", nextState);
        nextState = fsm.next("event3");
        Assertions.assertEquals("state4", nextState);

    }

    @Test
    public void testFSM_ValidConfigNullCurrentState() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:symmetricStateMachine.csv");
        File configFile = resource.getFile();
        Assertions.assertThrows(Exception.class, () -> {
            new FiniteStateMachine(configFile, null);
        });
    }

    @Test
    public void testFSM_ValidConfigEmptyCurrentState() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:symmetricStateMachine.csv");
        File configFile = resource.getFile();
        Assertions.assertThrows(Exception.class, () -> {
            new FiniteStateMachine(configFile, "");
        });
    }

    @Test
    public void testFSM_EmptyConfigFileValidState() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:emptyStateMachine.csv");
        File configFile = resource.getFile();
        Assertions.assertThrows(Exception.class, () -> {
            new FiniteStateMachine(configFile, "state1");
        });
    }

    @Test
    public void testFSM_nullConfigFileValidState() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            new FiniteStateMachine(null, "state1");
        });
    }
}