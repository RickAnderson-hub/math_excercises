# Math Exercises Generator

Generate printable math worksheets as PDFs with configurable operation sets (addition, subtraction, multiplication, division). Includes simple CLI and convenient Gradle tasks.

## Features
- Generates randomized math equations under a configurable upper bound (limit)
- Operation sets selectable: ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
- Outputs a neatly formatted PDF with placeholders to fill in
- Gradle tasks for common sets (Add/Sub and Mul/Div) and an aggregate task

## Requirements
- Java 17+
- Gradle Wrapper (included) or Gradle 8/9 compatible environment

## Build
```bash
./gradlew build
```
The runnable jar is produced at:
```
build/libs/math_excercises-1.0.0.jar
```

## Quick start

### Option A: Use Gradle tasks (recommended)
These tasks run the app with sensible defaults and distinct output filenames. The PDF layout uses 50 equations per column, so 200 exercises renders as 4 columns per page.

- Addition/Subtraction
```bash
./gradlew generateAddSub -Plimit=20 -PnumberOfExercises=200 -Piterations=1
```
Produces a PDF named like:
```
MathExercises_1_AddSub.pdf
```

- Multiplication/Division
```bash
./gradlew generateMulDiv -Plimit=12 -PnumberOfExercises=200 -Piterations=1
```
Produces a PDF named like:
```
MathExercises_1_MulDiv.pdf
```

- Aggregate (runs both)
```bash
./gradlew generateAll -Plimit=20 -PnumberOfExercises=200 -Piterations=1
```

Supported Gradle properties:
- `-Plimit` (default: 20) — max value used in exercises; must be >= 10
- `-PnumberOfExercises` or `-Pcount` (default: 200) — how many equations per sheet
- `-Piterations` (default: 1) — how many sheets to generate
- `-PoutputBaseName` (default: MathExercises) — base name for the output files

### Option B: Run the jar directly
```bash
java -jar build/libs/math_excercises-1.0.0.jar <limit> <numberOfExercises> <iterations> [operations]
```
- `limit` — upper bound for numbers/results (must be >= 10)
- `numberOfExercises` — how many equations to place on the sheet
- `iterations` — how many sheets to generate
- `operations` (optional) — comma-separated list from:
  - `ADDITION`, `SUBTRACTION`, `MULTIPLICATION`, `DIVISION`
  - If omitted or empty, defaults to `ADDITION,SUBTRACTION`.

Examples:
```bash
# Only addition and subtraction (4 columns)
java -jar build/libs/math_excercises-1.0.0.jar 20 200 1 ADDITION,SUBTRACTION

# Only multiplication (4 columns)
java -jar build/libs/math_excercises-1.0.0.jar 12 200 1 MULTIPLICATION

# Mixed (4 columns)
java -jar build/libs/math_excercises-1.0.0.jar 20 200 1 ADDITION,DIVISION
```

Output filename defaults to `MathExercises_<iteration>.pdf`. You can override via JVM properties:
```bash
java -DoutputBaseName=Worksheets -DoutputSuffix=_Custom \
     -jar build/libs/math_excercises-1.0.0.jar 20 200 1 ADDITION,SUBTRACTION
# -> Worksheets_1_Custom.pdf
```

Note: The Gradle tasks already set `-DoutputSuffix` to `_AddSub` or `_MulDiv` for distinct filenames.

## Behavior & constraints
- `limit >= 10` is enforced
- Numbers and results are non-negative and within the specified `limit`
- Division/multiplication avoid zero divisors and keep products/quotients within the `limit`

## Testing
Run the test suite:
```bash
./gradlew test
```

## Code coverage
- Coverage is enforced at 80% (lines). Data model classes are excluded from coverage metrics:
  - Excluded pattern: `**/org/rick/math_excercises/model/**`
- Generate the coverage report:
```bash
./gradlew jacocoTestReport
```
- Open the HTML report (Linux):
```bash
xdg-open build/reports/jacoco/test/html/index.html
```

## Troubleshooting
- If PDFs are not generated, ensure you have write permissions to the current directory.
- The font file `arialuni.ttf` is bundled under resources; if font loading fails, verify the resource exists in the jar.

## License
This project is licensed as Shareware by Rick Anderson (c) 2025. In summary:
- Personal, non-commercial use is free; redistribution of unmodified copies is allowed with attribution.
- Modified versions may be redistributed for non-commercial purposes with attribution and clear change notes.
- Any commercial use requires a paid commercial license or prior written permission from the author.
- Attribution should read: "Math Exercises Generator by Rick Anderson". For modified works: "Based on Math Exercises Generator by Rick Anderson".

See the full license terms in the `LICENSE` file. For commercial licensing or permissions, contact Rick Anderson at rick@getanderson.net.
