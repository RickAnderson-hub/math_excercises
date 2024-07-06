package org.rick.math_excercises.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ExerciseCommand {

    @ShellMethod
    public void generateExercises(@ShellOption int limit, @ShellOption int numberOfExercises) {
       //do nothing/  Bit of a hack :(
    }
}