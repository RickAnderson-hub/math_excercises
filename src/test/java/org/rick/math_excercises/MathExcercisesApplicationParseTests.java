package org.rick.math_excercises;

import org.junit.jupiter.api.Test;
import org.rick.math_excercises.service.Operations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MathExcercisesApplicationParseTests {

    @SuppressWarnings("unchecked")
    private static Set<Operations> invokeParse(String arg) {
        try {
            Method m = MathExcercisesApplication.class.getDeclaredMethod("parseOperationsArg", String.class);
            m.setAccessible(true);
            return (Set<Operations>) m.invoke(null, arg);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            if (cause instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException(cause);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parsesCommaSeparatedOperationsCaseInsensitive() {
        Set<Operations> ops = invokeParse(" addition, DIVISION ");
        assertTrue(ops.contains(Operations.ADDITION));
        assertTrue(ops.contains(Operations.DIVISION));
        assertEquals(2, ops.size());
    }

    @Test
    void defaultsToAddSubOnBlank() {
        Set<Operations> ops = invokeParse("   ");
        assertTrue(ops.contains(Operations.ADDITION));
        assertTrue(ops.contains(Operations.SUBTRACTION));
        assertEquals(2, ops.size());
    }

    @Test
    void throwsOnUnknownOperation() {
        assertThrows(IllegalArgumentException.class, () -> invokeParse("FOO"));
    }
}
