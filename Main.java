package battleship;

import java.util.Scanner;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        GameBoard[] playerBoards = new GameBoard[2];
        Scanner input = new Scanner(System.in);
        //Set up both boards
        for(int i = 0; i < 2; i++){
            playerBoards[i] = new GameBoard();
            System.out.println("Player "+ (i + 1) + ", place your ships on the game field");
            System.out.println();
            playerBoards[i].printEntireBoard();
            playerBoards[i].fiveShipSetup();
            //playerBoards[i].oneShipSetup();
            promptEnterKey();
        }

        //Start Game
        int[] shipsRemaining = new int[2];
        shipsRemaining[0] = playerBoards[0].getShipsLeft();
        shipsRemaining[1] = playerBoards[1].getShipsLeft();
        while(shipsRemaining[0] > 0 && shipsRemaining[1] > 0) {
            for(int i = 0; i < 2; i++){
                System.out.println("Player " + (i + 1) + ", it's your turn:");
                int otherBoard = (i + 1) % 2;
                int myBoard = i;
                playerBoards[otherBoard].printFoggedBoard();
                System.out.println("---------------------");
                playerBoards[myBoard].printEntireBoard();
                boolean successfulHit = false;
                while(!successfulHit){
                    successfulHit = playerBoards[otherBoard].takeShot(input.nextLine());
                }
                shipsRemaining[otherBoard] = playerBoards[otherBoard].getShipsLeft();
                if(shipsRemaining[1] == 0){
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }else if(shipsRemaining[0] == 0){
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }
                promptEnterKey();
            }
        }
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
