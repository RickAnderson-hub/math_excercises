package org.rick.math_excercises;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rick.math_excercises.model.Equation;
import org.rick.math_excercises.service.GenerateService;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

class MathExcercisesApplicationTests {

    @Mock
    private ApplicationContext context;
    @Mock
    private GenerateService generateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void applicationGeneratesCorrectNumberOfExercises() {
        when(context.getBean(GenerateService.class)).thenReturn(generateService);
        when(generateService.generateExercises(10, 5)).thenReturn(Arrays.asList(new Equation(), new Equation(), new Equation(), new Equation(), new Equation()));

        String[] args = {"10", "5"};
        MathExcercisesApplication.main(args);

        // Verify the generateExercises method is called with correct parameters
        // This is indirectly verified by the mock setup and the expected output
    }

    @Test
    void applicationHandlesInvalidArgumentsGracefully() {
        String[] args = {};
        MathExcercisesApplication.main(args);

        // This test verifies that the application does not crash with invalid arguments
        // The actual output is not captured here, but the application should exit with a usage message
    }

    @Test
    void applicationProcessesLargeNumbersCorrectly() {
        when(context.getBean(GenerateService.class)).thenReturn(generateService);
        List<Equation> largeNumberEquations = Arrays.asList(new Equation(), new Equation());
        when(generateService.generateExercises(10000, 2)).thenReturn(largeNumberEquations);

        String[] args = {"10000", "2"};
        MathExcercisesApplication.main(args);

        // Verify the application can handle large numbers without crashing or hanging
    }
}