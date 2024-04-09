import java.util.*;
public class TicTacToe {

    static int playerScore = 0;
    static int computerScore = 0;
    static Scanner input = new Scanner(System.in);
/*
    Notes:

        _ | _ | _
        _ | _ | _
          |   |

         Helpful indices

         [0][0] , [0][2] , [0][4]
         [1][0] , [1][2] , [1][4]
         [2][0] , [2][2] , [2][4]

        Player = 1
        Computer = 2

 */

public static void main(String [] args){

    char [][] gameBoard = {{'_','|','_','|','_'},{'_','|','_','|','_'},{' ', '|', ' ','|',' '}};
    printBoard(gameBoard);
    boolean gameOver = false;
    boolean playAgain = true;
    System.out.println("!!!!Welcome to Tic Tac Toe!!!!");

  while(playAgain) {
      while (!gameOver) {
        playerMove(gameBoard);
        gameOver = isGameOver(gameBoard);
        if (gameOver) {
            break;
        }
        System.out.println("------");
        computerMove(gameBoard);
        gameOver = isGameOver(gameBoard);
        if (gameOver) {
            break;
        }
      }
      System.out.println("Player Score: " +playerScore);
      System.out.println("Computer Score: "+ computerScore);
      System.out.println("Would you like to play again? Y/N");
      input.nextLine();
      String result = input.nextLine();

      switch (result){
          case "Y":
          case "y":
              playAgain = true;
              System.out.println("Yes! Let's play again");
              resetBoard(gameBoard);
              gameOver = false;
              printBoard(gameBoard);
              break;

          case "N":
          case "n":
              playAgain = false;
              System.out.println("Thanks for playing");
              break;
          default:
              break;
      }

  }
}


public static void printBoard(char [][] gameBoard){

    for(char[] row : gameBoard){
        for( char c : row){
            System.out.print(c);
        }
        System.out.println();
    }
}

public static List<int[]> getValidMoves(char [][] gameBoard){
    List<int[]> emptyCellsList= new ArrayList<int []>();
    for (int i=0;i<3;i++){
        for (int j=0;j<5;j++){
            if (gameBoard[i][j] == '_' || gameBoard[i][j]==' '){
                emptyCellsList.add(new int[]{i,j});
            }
        }
    }
    return emptyCellsList;

}

public static void updateBoard( int position, int player, char [][] gameBoard){

        char character;

        if(player==1){
            character = 'X';
        }else{
            character = 'O';
        }

        switch (position){

            case 1:
                gameBoard[0][0] = character;
                printBoard(gameBoard);
                break;
            case 2:
                gameBoard[0][2] = character;
                printBoard(gameBoard);
                break;
            case 3:
                gameBoard[0][4] = character;
                printBoard(gameBoard);
                break;
            case 4:
                gameBoard[1][0] = character;
                printBoard(gameBoard);
                break;
            case 5:
                gameBoard[1][2] = character;
                printBoard(gameBoard);
                break;
            case 6:
                gameBoard[1][4] = character;
                printBoard(gameBoard);
                break;
            case 7:
                gameBoard[2][0] = character;
                printBoard(gameBoard);
                break;
            case 8:
                gameBoard[2][2] = character;
                printBoard(gameBoard);
                break;
            case 9:
                gameBoard[2][4] = character;
                printBoard(gameBoard);
                break;
            default:
                break;

        }

}


public static void playerMove(char[][] gameBoard){

    System.out.println("Please make a move. (1-9)");


    int move = input.nextInt();

    boolean result = isValidMove(move,gameBoard);

    while(!result){
        System.out.println("Sorry! Invalid Move. Try again");
        move = input.nextInt();
        result = isValidMove(move,gameBoard);
    }

    System.out.println("Player moved at position " + move);
    updateBoard(move,1,gameBoard);


}


public static boolean isValidMove(int move, char[][] gameboard){

    switch (move){
        case 1:
            if(gameboard[0][0] == '_'){
                return true;
            } else{
                return false;
            }
        case 2:
            if(gameboard[0][2] == '_'){
                return true;
            } else{
                return false;
            }
        case 3:
            if(gameboard[0][4] == '_'){
                return true;
            } else{
                return false;
            }

        case 4:
            if(gameboard[1][0] == '_'){
                return true;
            } else{
                return false;
            }
        case 5:
            if(gameboard[1][2] == '_'){
                return true;
            } else{
                return false;
            }
        case 6:
            if(gameboard[1][4] == '_'){
                return true;
            } else{
                return false;
            }
        case 7:
            if(gameboard[2][0] == ' '){
                return true;
            } else{
                return false;
            }
        case 8:
            if(gameboard[2][2] == ' '){
                return true;
            } else{
                return false;
            }
        case 9:
            if(gameboard[2][4] == ' '){
                return true;
            } else{
                return false;
            }

        default:
            return false;
    }

}
/**
 * Clones the provided array
 * 
 * @param src
 * @return a new clone of the provided array
 */
public static char[][] cloneArray(char[][] src) {
    int length = src.length;
    char[][] target = new char[length][src[0].length];
    for (int i = 0; i < length; i++) {
        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
    }
    return target;
}

public static int[] minimaxDecision(char [][] gameBoard){
    Double currentUtility=Double.NEGATIVE_INFINITY;    
    List <int []> emptyCellList=getValidMoves(gameBoard);
    int [] moveList=new int[2];
    Double cutOff=Double.POSITIVE_INFINITY;
    for (int[] row: emptyCellList){
        Double alpha=Double.NEGATIVE_INFINITY;
        Double beta=Double.POSITIVE_INFINITY;
        char [][] copyBoard=cloneArray(gameBoard);
        int move=indexToMove(row);
        updateBoard(move, 2, copyBoard); 
        Double utility=minValue(copyBoard,alpha,beta,cutOff);
        
        if (utility>currentUtility){
            currentUtility=utility;
            moveList=row;
        }
    }
    return moveList;
}

public static Double minValue(char[][] gameBoard, Double alpha, Double beta, Double cutOff) {
    if (checkTerminalState(gameBoard)) {
        return evaluationFunction(gameBoard);
    } 
    Double max_value = Double.POSITIVE_INFINITY;
    for (int[] move : getValidMoves(gameBoard)) {
        char[][] copyBoard = cloneArray(gameBoard);
        updateBoard(indexToMove(move), 1, copyBoard); 
        Double result = maxValue(copyBoard, alpha, beta, cutOff);
        max_value = Math.min(max_value, result);
        if (max_value <= alpha) {
            break; // Pruning
        }
        beta = Math.min(beta, max_value);
    }
    return max_value;
}

public static Double maxValue(char[][] gameBoard, Double alpha, Double beta, Double cutOff) {
    if (checkTerminalState(gameBoard)) {
        return evaluationFunction(gameBoard);
    } 
    Double min_value = Double.NEGATIVE_INFINITY;
    for (int[] move : getValidMoves(gameBoard)) {
        char[][] copyBoard = cloneArray(gameBoard);
        updateBoard(indexToMove(move), 2, copyBoard); 
        Double result = minValue(copyBoard, alpha, beta, cutOff);
        min_value = Math.max(min_value, result);
        if (min_value >= beta) {
            break; // Pruning
        }
        alpha = Math.max(alpha, min_value);
    }
    return min_value;
}

public static Double evaluationFunctionButBetter(char[][] gameBoard) {
    double score = 0.0;

    // Center control
    score += (gameBoard[1][2] == 'O' ? 5.0 : 0.0) - (gameBoard[1][2] == 'X' ? 5.0 : 0.0);

    // Evaluate two-in-a-row scenarios for both 'O' and 'X'
    score += evaluateTwoInARow(gameBoard, 'O') * 10.0; // AI's opportunities
    score -= evaluateTwoInARow(gameBoard, 'X') * 15.0; // Opponent's opportunities, higher weight to block;

    return score;
}

public static double evaluateTwoInARow(char[][] gameBoard, char player) {
    double score = 0.0;
    // Scores for two-in-a-row scenarios
    double twoInARowScore = 2.0;

    // Check all rows, columns, and diagonals for two-in-a-row scenarios
    for (int i = 0; i < 3; i++) {
        // Rows
        score += evaluateLine(gameBoard[i][0], gameBoard[i][2], gameBoard[i][4], player) ? twoInARowScore : 0.0;
        // Columns (Note: Adjust the indices according to your board representation)
        score += evaluateLine(gameBoard[0][i*2], gameBoard[1][i*2], gameBoard[2][i*2], player) ? twoInARowScore : 0.0;
    }
    // Diagonals
    score += evaluateLine(gameBoard[0][0], gameBoard[1][2], gameBoard[2][4], player) ? twoInARowScore : 0.0;
    score += evaluateLine(gameBoard[0][4], gameBoard[1][2], gameBoard[2][0], player) ? twoInARowScore : 0.0;

    return score;
}

private static boolean evaluateLine(char c1, char c2, char c3, char player) {
    // Count 'player' symbols and empty spaces in the line
    int playerCount = 0, emptyCount = 0;
    if (c1 == player) playerCount++;
    else if (c1 == '_' || c1 == ' ') emptyCount++;

    if (c2 == player) playerCount++;
    else if (c2 == '_' || c2 == ' ') emptyCount++;

    if (c3 == player) playerCount++;
    else if (c3 == '_' || c3 == ' ') emptyCount++;

    // A two-in-a-row scenario with an open cell
    return (playerCount == 2 && emptyCount == 1);
}


public static boolean checkTerminalState(char[][] gameBoard) {
    // Check for win
    if (evaluateWin(gameBoard, 'X')) return true; 
    if (evaluateWin(gameBoard, 'O')) return true; 

    // Check for draw
    for (int i = 0; i < gameBoard.length; i++) {
        for (int j = 0; j < gameBoard[0].length; j += 2) { 
            if (gameBoard[i][j] == '_' || gameBoard[i][j] == ' ') {
                return false; 
            }
        }
    }
    // No moves left, it's a draw
    return true;
}

public static boolean evaluateWin(char[][] gameBoard, char player) {
    
    return (checkLine(gameBoard[0][0], gameBoard[0][2], gameBoard[0][4], player) ||
            checkLine(gameBoard[1][0], gameBoard[1][2], gameBoard[1][4], player) ||
            checkLine(gameBoard[2][0], gameBoard[2][2], gameBoard[2][4], player) ||
            checkLine(gameBoard[0][0], gameBoard[1][0], gameBoard[2][0], player) ||
            checkLine(gameBoard[0][2], gameBoard[1][2], gameBoard[2][2], player) ||
            checkLine(gameBoard[0][4], gameBoard[1][4], gameBoard[2][4], player) ||
            checkLine(gameBoard[0][0], gameBoard[1][2], gameBoard[2][4], player) ||
            checkLine(gameBoard[0][4], gameBoard[1][2], gameBoard[2][0], player));
}

public static boolean checkLine(char c1, char c2, char c3, char player) {
    return (c1 == player) && (c2 == player) && (c3 == player);
}





public static Double evaluationFunction(char [][] gameboard){
        //Horizontal Win
        if(gameboard[0][0] == 'X'&& gameboard[0][2] == 'X' && gameboard [0][4] == 'X' ){            
            return (double) -1;
        }
        if(gameboard[0][0] == 'O'&& gameboard[0][2] == 'O' && gameboard [0][4] == 'O' ){
            return (double) +1;
            
        }
        if(gameboard[1][0] == 'X'&& gameboard[1][2] == 'X' && gameboard [1][4] == 'X' ){
            return (double) -1;
        }
        if(gameboard[1][0] == 'O'&& gameboard[1][2] == 'O' && gameboard [1][4] == 'O' ){
            return (double) +1;
        }
        if(gameboard[2][0] == 'X'&& gameboard[2][2] == 'X' && gameboard [2][4] == 'X' ){
            return (double) -1;
        }
        if(gameboard[2][0] == 'O'&& gameboard[2][2] == 'O' && gameboard [2][4] == 'O' ) {
            return (double) +1;
        }
    
        //Vertical Wins
    
            if(gameboard[0][0] == 'X'&& gameboard[1][0] == 'X' && gameboard [2][0] == 'X' ){
                return (double) -1;
            }
            if(gameboard[0][0] == 'O'&& gameboard[1][0] == 'O' && gameboard [2][0] == 'O' ){
                return (double) +1;
            }
    
            if(gameboard[0][2] == 'X'&& gameboard[1][2] == 'X' && gameboard [2][2] == 'X' ){
                return (double) -1;
            }
            if(gameboard[0][2] == 'O'&& gameboard[1][2] == 'O' && gameboard [2][2] == 'O' ){
                return (double) +1;
            }
    
            if(gameboard[0][4] == 'X'&& gameboard[1][4] == 'X' && gameboard [2][4] == 'X' ){
                return (double) -1;
            }
            if(gameboard[0][4] == 'O'&& gameboard[1][4] == 'O' && gameboard [2][4] == 'O' ){
                return (double) +1;
            }
    
            //Diagonal Wins
            if(gameboard[0][0] == 'X'&& gameboard[1][2] == 'X' && gameboard [2][4] == 'X' ){
                return (double) -1;
            }
            if(gameboard[0][0] == 'O'&& gameboard[1][2] == 'O' && gameboard [2][4] == 'O' ){
                return (double) +1;
            }
    
            if(gameboard[2][0] == 'X'&& gameboard[1][2] == 'X' && gameboard [0][4] == 'X' ){
                return (double) -1;
            }
            if(gameboard[2][0] == 'O'&& gameboard[1][2] == 'O' && gameboard [0][4] == 'O' ){
                return (double) +1;
            }
    
            if(gameboard[0][0] != '_' && gameboard[0][2] != '_' && gameboard[0][4] != '_' && gameboard[1][0] !='_'&&
                gameboard[1][2] != '_' && gameboard[1][4] != '_' && gameboard[2][0] != ' ' && gameboard[2][2] != ' ' && gameboard[2][4] != ' '){
                return (double) 0;
            }
    
    return (double) 0;
}

public static int indexToMove(int [] moveList){
    if (moveList[0]==0 && moveList[1]==0){
        return 1;
    }

    if (moveList[0]==0 && moveList[1]==2){
        return 2;
    }

    if (moveList[0]==0 && moveList[1]==4){
        return 3;
    }

    if (moveList[0]==1 && moveList[1]==0){
        return 4;
    }

    if (moveList[0]==1 && moveList[1]==2){
        return 5;
    }

    if (moveList[0]==1 && moveList[1]==4){
        return 6;
    }

    if (moveList[0]==2 && moveList[1]==0){
        return 7;
    }

    if (moveList[0]==2 && moveList[1]==2){
        return 8;
    }

    if (moveList[0]==2 && moveList[1]==4){
            return 9;    
        }
    
        return 0;

}
public static void computerMove(char [][] gameBoard){
    int[] nextMove=minimaxDecision(gameBoard);
    int move=indexToMove((nextMove));
    System.out.println("Computer moved at position "+ move);
    updateBoard(move,2,gameBoard);
}


public static boolean isGameOver(char [][] gameboard){

    //Horizontal Win
    if(gameboard[0][0] == 'X'&& gameboard[0][2] == 'X' && gameboard [0][4] == 'X' ){
        System.out.println("Player Wins");
        playerScore++;
        return true;
    }
    if(gameboard[0][0] == 'O'&& gameboard[0][2] == 'O' && gameboard [0][4] == 'O' ){
        System.out.println("Computer Wins");
        computerScore++;
        return true;
    }
    if(gameboard[1][0] == 'X'&& gameboard[1][2] == 'X' && gameboard [1][4] == 'X' ){
        System.out.println("Player Wins");
        playerScore++;
        return true;
    }
    if(gameboard[1][0] == 'O'&& gameboard[1][2] == 'O' && gameboard [1][4] == 'O' ){
        System.out.println("Computer Wins");
        computerScore++;
        return true;
    }
    if(gameboard[2][0] == 'X'&& gameboard[2][2] == 'X' && gameboard [2][4] == 'X' ){
        System.out.println("Player Wins");
        playerScore++;
        return true;
    }
    if(gameboard[2][0] == 'O'&& gameboard[2][2] == 'O' && gameboard [2][4] == 'O' ) {
        System.out.println("Computer Wins");
        computerScore++;
        return true;
    }

    //Vertical Wins

        if(gameboard[0][0] == 'X'&& gameboard[1][0] == 'X' && gameboard [2][0] == 'X' ){
            System.out.println("Player Wins");
            playerScore++;
            return true;
        }
        if(gameboard[0][0] == 'O'&& gameboard[1][0] == 'O' && gameboard [2][0] == 'O' ){
            System.out.println("Computer Wins");
            computerScore++;
            return true;
        }

        if(gameboard[0][2] == 'X'&& gameboard[1][2] == 'X' && gameboard [2][2] == 'X' ){
            System.out.println("Player Wins");
            playerScore++;
            return true;
        }
        if(gameboard[0][2] == 'O'&& gameboard[1][2] == 'O' && gameboard [2][2] == 'O' ){
            System.out.println("Computer Wins");
            computerScore++;
            return true;
        }

        if(gameboard[0][4] == 'X'&& gameboard[1][4] == 'X' && gameboard [2][4] == 'X' ){
            System.out.println("Player Wins");
            playerScore++;
            return true;
        }
        if(gameboard[0][4] == 'O'&& gameboard[1][4] == 'O' && gameboard [2][4] == 'O' ){
            System.out.println("Computer Wins");
            computerScore++;
            return true;
        }

        //Diagonal Wins
        if(gameboard[0][0] == 'X'&& gameboard[1][2] == 'X' && gameboard [2][4] == 'X' ){
            System.out.println("Player Wins");
            playerScore++;
            return true;
        }
        if(gameboard[0][0] == 'O'&& gameboard[1][2] == 'O' && gameboard [2][4] == 'O' ){
            System.out.println("Computer Wins");
            computerScore++;
            return true;
        }

        if(gameboard[2][0] == 'X'&& gameboard[1][2] == 'X' && gameboard [0][4] == 'X' ){
            System.out.println("Player Wins");
            playerScore++;
            return true;
        }
        if(gameboard[2][0] == 'O'&& gameboard[1][2] == 'O' && gameboard [0][4] == 'O' ){
            System.out.println("Computer Wins");
            computerScore++;
            return true;
        }

        if(gameboard[0][0] != '_' && gameboard[0][2] != '_' && gameboard[0][4] != '_' && gameboard[1][0] !='_'&&
            gameboard[1][2] != '_' && gameboard[1][4] != '_' && gameboard[2][0] != ' ' && gameboard[2][2] != ' ' && gameboard[2][4] != ' '){
            System.out.println("Its a tie");
            return true;
        }
    return false;
}

public static void resetBoard(char [][] gameBoard){
    gameBoard[0][0] = '_';
    gameBoard[0][2] = '_';
    gameBoard[0][4] = '_';
    gameBoard[1][0] = '_';
    gameBoard[1][2] = '_';
    gameBoard[1][4] = '_';
    gameBoard[2][0] = ' ';
    gameBoard[2][2] = ' ';
    gameBoard[2][4] = ' ';

}
}

