import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SudokuSolverTest {

    private SudokuSolver solver;
    private final static int[][] solvableBoard = {
            {7, 8, 0, 4, 0, 0, 1, 2, 0},
            {6, 0, 0, 0, 7, 5, 0, 0, 9},
            {0, 0, 0, 6, 0, 1, 0, 7, 8},
            {0, 0, 7, 0, 4, 0, 2, 6, 0},
            {0, 0, 1, 0, 5, 0, 9, 3, 0},
            {9, 0, 4, 0, 6, 0, 0, 0, 5},
            {0, 7, 0, 3, 0, 0, 0, 1, 2},
            {1, 2, 0, 0, 0, 7, 4, 0, 0},
            {0, 4, 9, 2, 0, 6, 0, 0, 7}
    };

    private final static int[][] solvedBoard = {
            {7, 8, 5, 4, 3, 9, 1, 2, 6},
            {6, 1, 2, 8, 7, 5, 3, 4, 9},
            {4, 9, 3, 6, 2, 1, 5, 7, 8},
            {8, 5, 7, 9, 4, 3, 2, 6, 1},
            {2, 6, 1, 7, 5, 8, 9, 3, 4},
            {9, 3, 4, 1, 6, 2, 7, 8, 5},
            {5, 7, 8, 3, 9, 4, 6, 1, 2},
            {1, 2, 6, 5, 8, 7, 4, 9, 3},
            {3, 4, 9, 2, 1, 6, 8, 5, 7}
    };

    @BeforeEach
    void init() {
        solver = new SudokuSolver(solvableBoard);
    }

    @Nested
    class TestValid {
        @Test
        void testEmptyValid() {
            assertAll(
                    () -> assertTrue(solver.valid(solvableBoard, 3, 0, 2)),
                    () -> assertTrue(solver.valid(solvableBoard, 1, 3, 8)),
                    () -> assertTrue(solver.valid(solvableBoard, 3, 3, 0))
            );
        }

        @Test
        void testEmptyInvalid() {
            assertAll(
                    () -> assertFalse(solver.valid(solvableBoard, 9, 1, 2)),
                    () -> assertFalse(solver.valid(solvableBoard, 5, 7, 8)),
                    () -> assertFalse(solver.valid(solvableBoard, 6, 5, 5))
            );
        }
    }

    @Test
    void testSolve() {
        List<int[][]> list = solver.returnList();
        assertArrayEquals(list.get(list.size() - 1), solvedBoard);
    }


}