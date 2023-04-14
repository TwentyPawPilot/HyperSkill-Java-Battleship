package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class GameBoard {
    public final static String[] TOP_HEADER = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public final static String[] LEFT_LABELS = {" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private int shipsOnTheField;

    private String[][] field, foggedField;

    public GameBoard() {
        //
        this.field = new String[11][11];
        this.foggedField = new String[11][11];
        for (int i = 0; i < 11; i++) {
            Arrays.fill(this.field[i], "~");
            Arrays.fill(this.foggedField[i], "~");
        }
        for (int i = 0; i < 11; i++) {
            this.field[0][i] = TOP_HEADER[i];
            this.field[i][0] = LEFT_LABELS[i];
            this.foggedField[0][i] = TOP_HEADER[i];
            this.foggedField[i][0] = LEFT_LABELS[i];
        }
        this.shipsOnTheField = 0;
    }

    public void printEntireBoard() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(this.field[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printFoggedBoard() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(this.foggedField[i][j] + " ");
            }
            System.out.println();
        }
    }

    protected static int countLength(String startCoords, String endCoords) {
        if (!coordinateIsValid(startCoords) || !coordinateIsValid(endCoords)) {
            System.out.println("Error! Coordinates are invalid.");
            return -1;
        }
        int verticalDistance = " ABCDEFGHIJ".indexOf(startCoords.charAt(0)) - " ABCDEFGHIJ".indexOf(endCoords.charAt(0));
        verticalDistance = Math.abs(verticalDistance);

        int firstHoriz = Integer.parseInt(String.valueOf(startCoords.charAt(startCoords.length() - 1)));
        firstHoriz = firstHoriz == 0 ? 10 : firstHoriz;

        int secondHoriz = Integer.parseInt(String.valueOf(endCoords.charAt(endCoords.length() - 1)));
        secondHoriz = secondHoriz == 0 ? 10 : secondHoriz;

        int horizontalDistance = Math.abs(firstHoriz - secondHoriz);
        if (horizontalDistance == 0 && verticalDistance == 0) {
            System.out.println("Error! These coordinates are the same.");
            return -1;
        } else if (horizontalDistance == 0 ^ verticalDistance == 0) {
            return horizontalDistance + verticalDistance + 1;
        } else {
            //System.out.println("Error! These coordinates are not horizontal or vertical");
            return -2;
        }
    }

    private static boolean coordinateIsValid(String coordinate) {
        return coordinate.matches("[A-J](10|[1-9])\\b");
    }

    protected boolean addShip(String startCoord, String endCoord) {
        int shipLength = countLength(startCoord, endCoord);
        if (shipLength == -1) {
            return false;
        }
        //Vertical ship
        int column = Integer.parseInt(String.valueOf(startCoord.charAt(startCoord.length() - 1)));
        column = column == 0 ? 10 : column;
        if (startCoord.charAt(startCoord.length() - 1) == endCoord.charAt(endCoord.length() - 1)) {
            int startingRow = Math.min(" ABCDEFGHIJ".indexOf(startCoord.charAt(0)), " ABCDEFGHIJ".indexOf(endCoord.charAt(0)));
            // Check for overlapping ships
            for (int i = startingRow; i < startingRow + shipLength; i++) {
                if (overlappingShips(i, column)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }

            for (int i = startingRow; i < startingRow + shipLength; i++) {
                this.field[i][column] = "O";
            }
            this.shipsOnTheField += 1;
            return true;
        }
        //Horizontal ship
        if (startCoord.charAt(0) == endCoord.charAt(0)) {
            int rowIndex = " ABCDEFGHIJ".indexOf(startCoord.charAt(0));
            int firstCol = column;
            firstCol = firstCol == 0 ? 10 : firstCol;
            int secondCol = Integer.parseInt(String.valueOf(endCoord.charAt(endCoord.length() - 1)));
            secondCol = secondCol == 0 ? 10 : secondCol;
            int startCol = Math.min(firstCol, secondCol);
            // Check for overlapping ships
            for (int i = startCol; i < startCol + shipLength; i++) {
                if (overlappingShips(rowIndex, i)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
            for (int i = startCol; i < startCol + shipLength; i++) {
                this.field[rowIndex][i] = "O";
            }
            this.shipsOnTheField += 1;
            return true;
        }
        System.out.println("If the program gets to here, something has gone wrong.");
        return false;
    }

    private boolean overlappingShips(int row, int column) {
        boolean tooClose = false;
        for (int i = -1; i <= 1; i++) {
            try {
                tooClose = this.field[row + i][column].equals("O");
                if (tooClose) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        for (int i = -1; i <= 1; i++) {
            try {
                tooClose = this.field[row][column + i].equals("O");
                if (tooClose) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        //If it gets to this point, all values are good and a false call can be made.
        return false;
    }

    protected void addSpecificShip(String name, int length) {
        boolean success = false;
        Scanner input = new Scanner(System.in);
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", name, length);
        do {
            System.out.println();
            String[] playerCoords = input.nextLine().split(" ");
            int checkLength = countLength(playerCoords[0], playerCoords[1]);
            if (checkLength == -2) {
                //Improper position error
                System.out.println("Error! Wrong ship location! Try again:");
                System.out.println();
                continue;
            } else if (checkLength != length) {
                System.out.printf("Error! Wrong length of the %s! Try again:\n", name);
                System.out.println();
                continue;
            } else {
                success = this.addShip(playerCoords[0], playerCoords[1]);
                System.out.println();
            }
        } while (!success);
        this.printEntireBoard();
    }

    protected boolean takeShot(String coordinate) {
        if (!coordinateIsValid(coordinate)) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
        int[] locations = coordsToIndex(coordinate);
        if (this.field[locations[0]][locations[1]].equals("~") || this.field[locations[0]][locations[1]].equals("M")) {
            this.field[locations[0]][locations[1]] = "M";
            this.foggedField[locations[0]][locations[1]] = "M";
            //this.printFoggedBoard();
            System.out.println("You missed!");
        } else {
            boolean wasShipSank = sankShip(coordinate);
            String message = wasShipSank ? "You sank a ship!" : "You hit a ship!";
            this.field[locations[0]][locations[1]] = "X";
            this.foggedField[locations[0]][locations[1]] = "X";
            //this.printFoggedBoard();
            System.out.println(message);
            System.out.println();
        }
        System.out.println();
        return true;
    }

    public static int[] coordsToIndex(String coordinates) {
        int[] coords = new int[2];
        coords[0] = " ABCDEFGHIJ".indexOf(coordinates.charAt(0));
        coords[1] = Integer.parseInt(String.valueOf(coordinates.charAt(coordinates.length() - 1)));
        coords[1] = coords[1] == 0 ? 10 : coords[1];
        return coords;
    }

    private boolean sankShip(String coordinate) {
        int coords[] = coordsToIndex(coordinate);
        int row = coords[0];
        int column = coords[1];
        boolean hasSank = true;
        String testChar = "";
        //check north
        int offset = 0;
        int counter = row;
        while (counter > 0) {
            testChar = this.field[row + --offset][column];
            if (testChar.equals("X")) {
                counter--;
                continue;
            } else if (!testChar.equals("O")) {
                break;
            } else {
                hasSank = false;
                return false;
            }
        }
        //check south
        offset = 1;
        counter = row;
        while (counter + offset < 11) {
            testChar = this.field[row + offset++][column];
            if (testChar.equals("X")) {
                counter++;
                continue;
            } else if (!testChar.equals("O")) {
                break;
            } else {
                hasSank = false;
                return false;
            }
        }
        //check east
        offset = 1;
        counter = column;
        while (counter + offset < 11) {
            testChar = this.field[row][column + offset++];
            if (testChar.equals("X")) {
                counter++;
                continue;
            } else if (!testChar.equals("O")) {
                break;
            } else {
                hasSank = false;
                return false;
            }
        }
        //check west
        offset = 0;
        counter = column;
        while (counter > 0) {
            testChar = this.field[row][column + --offset];
            if (testChar.equals("X")) {
                counter--;
                continue;
            } else if (!testChar.equals("O")) {
                break;
            } else {
                hasSank = false;
                return false;
            }
        }
        //all tests passed, ship has sank
        //System.out.println("You sank a ship! Specify a new target:");
        //System.out.println();
        if(this.field[row][column].equals("O")) {
            this.shipsOnTheField--;
            return true;
        }else{
            //Repeat hit, no sink
            return false;
        }
    }

    public int getShipsLeft(){
        return this.shipsOnTheField;
    }

    public void fiveShipSetup(){
        this.addSpecificShip("Aircraft Carrier", 5);
        this.addSpecificShip("Battleship", 4);
        this.addSpecificShip("Submarine", 3);
        this.addSpecificShip("Cruiser", 3);
        this.addSpecificShip("Destroyer", 2);
    }

    public void oneShipSetup(){
        this.addSpecificShip("Aircraft Carrier", 2);
    }
}
