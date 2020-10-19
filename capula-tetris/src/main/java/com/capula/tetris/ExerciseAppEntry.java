package com.capula.tetris;

import com.capula.tetris.engine.SimpleTetrisSimulator;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Main entry point.
 */
public class ExerciseAppEntry {

    static InputStream inputStream = ExerciseAppEntry.class.getResourceAsStream("/input.txt");

    public static void main(String[] args) throws Exception {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            try (FileWriter writer = new FileWriter("output.txt")) {
                String line;
                while ((line = reader.readLine()) != null) {
                    var input = line.trim();
                    var tetris = new SimpleTetrisSimulator(10, 100);
                    var result = tetris.simulateSequence(input);
                    System.out.println(input + " height: " + result.getNonEmptyRows());
                    writer.write(result.getNonEmptyRows() + "\n");
                }
                writer.close();
                reader.close();
            }
        }
    }

}
