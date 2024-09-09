public class Simplex_Tabular {

    // Method to perform the Simplex Algorithm
    public static void simplex() {
        // Number of variables and constraints
        int numVariables = 2;
        int numConstraints = 2;

        // Coefficients of the objective function (maximize Z = x + y)
        double[] objective = {1, 1}; // Objective function coefficients

        // Coefficients of the constraints
        double[][] constraints = {
              {1, 1, 0, 1, 8},  // x + y + s4 = 8
              {2, 1, 1, 0, 10},  // 2x + y + s3 = 10
        };

        // Convert to tableau form
        double[][] tableau = new double[numConstraints + 1][numVariables + numConstraints + 1];

         for (int i = 0; i < numVariables; i++) {
            tableau[numConstraints][i] = -objective[i]; // Negate for maximization
        }
        tableau[numConstraints][numVariables + numConstraints] = 0; // Objective function value

        // Fill the tableau with constraints and objective function
        for (int i = 0; i < numConstraints; i++) {
            for (int j = 0; j < numVariables + numConstraints; j++) {
                tableau[i][j] = constraints[i][j];
            }
            tableau[i][numVariables + numConstraints] = constraints[i][numVariables + numConstraints];
        }

         printTableau(tableau);
        
        // Perform the Simplex algorithm
        simplexAlgorithm(tableau, numVariables, numConstraints);
    }

    // Simplex algorithm implementation
    public static void simplexAlgorithm(double[][] tableau, int numVariables, int numConstraints) {
        int rowCount = numConstraints + 1;
        int colCount = numVariables + numConstraints + 1;
        
        while (true) {
            // Find pivot column (entering variable)
            int pivotCol = -1;
            for (int j = 0; j < colCount - 1; j++) {
                if (tableau[numConstraints][j] < 0) {
                    if (pivotCol == -1 || tableau[numConstraints][j] < tableau[numConstraints][pivotCol]) {
                        pivotCol = j;
                    }
                }
            }
            
            if (pivotCol == -1) {
                break; // Optimal solution found
            }

            // Find pivot row (leaving variable)
            int pivotRow = -1;
            double minRatio = Double.MAX_VALUE;
            for (int i = 0; i < numConstraints; i++) {
                if (tableau[i][pivotCol] > 0) {
                    double ratio = tableau[i][colCount - 1] / tableau[i][pivotCol];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        pivotRow = i;
                    }
                }
            }

            if (pivotRow == -1) {
                throw new ArithmeticException("Unbounded solution");
            }

            // Pivot
            double pivotValue = tableau[pivotRow][pivotCol];
            for (int j = 0; j < colCount; j++) {
                tableau[pivotRow][j] /= pivotValue;
            }

            for (int i = 0; i < rowCount; i++) {
                if (i != pivotRow) {
                    double factor = tableau[i][pivotCol];
                    for (int j = 0; j < colCount; j++) {
                        tableau[i][j] -= factor * tableau[pivotRow][j];
                    }
                }
            }

            // Print the tableau for debugging
            printTableau(tableau);
        }

        // Print the final solution
        printSolution(tableau, numVariables, numConstraints);
    }

    // Method to print the tableau
    public static void printTableau(double[][] tableau) {
        for (double[] row : tableau) {
            for (double value : row) {
                System.out.printf("%10.2f ", value);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Method to print the final solution
    public static void printSolution(double[][] tableau, int numVariables, int numConstraints) {
        System.out.println("Optimal solution:");
        for (int i = 0; i < numVariables; i++) {
            double value = 0;
            for (int j = 0; j < numConstraints; j++) {
                if (Math.abs(tableau[j][i] - 1) < 1e-6) {  // Check for basic variable
                    value = tableau[j][tableau[0].length - 1];
                    break;
                }
            }
            System.out.printf("x%d = %.2f\n", i + 1, value);
        }
        System.out.printf("Z = %.2f\n", tableau[numConstraints][tableau[0].length - 1]);
    }

    public static void main(String[] args) {
        simplex();
    }
}
