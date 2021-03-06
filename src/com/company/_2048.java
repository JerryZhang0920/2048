package com.company;
import java.io.*;
import java.util.*;
// Name: Jerry Zhang
// Date: Feb.10 2021 - Mar.9 2021
// Project name: 2048 with file io
// purpose: make an unbreakable 2048 game!
public class _2048 {
    // initialize global variables
    public static final String ANSI_RESET = "\u001B[0m"; //from      https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    static String user_name = "";
    static int userline = 0;
    static int[][] grid = new int[4][4];
    static int tileint[] = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
    static int score = 0;
        // main method
    public static void main(String[] args) throws IOException {
        do {
            User_Access();
            String filename = "C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\users.txt";
            ArrayList<String> users = Read_user(filename);
            String gridfile = "C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\" + (user_name) + "_grid.txt";
            for (int i = 0; i < users.size(); i++) {
                if (user_name.equals(users.get(i))) {
                    System.out.println(ANSI_GREEN+"Username exists"+ANSI_RESET);
                    break;
                } else if (i == users.size() - 1) {
                    System.out.println(ANSI_CYAN+"New User Added"+ANSI_RESET);
                    new_user(filename);
                    Random_Tile();
                }
            }
            score = read_user_score(grid, gridfile);
            startGame(grid = check_user_file_exist(gridfile), gridfile);
        }while(contiune());
    }
    // User_Access: Let the user input their username
    // @param: none
    //@return: void
    public static void User_Access() {
            Scanner sc = new Scanner(System.in);
            System.out.println(ANSI_CYAN+"Welcome to 2048 game, please type in your username below"+ANSI_RESET);
            boolean flag_user = false;
            while (!flag_user) {
                try {
                    user_name = sc.next();
                    flag_user = true;
                    System.out.println(ANSI_CYAN+user_name + " Welcome to the 2048 game"+ANSI_RESET);

                } catch (InputMismatchException e) {
                    sc.next();
                    System.out.println("Please input a string");
                }
            }

    }
    // Read_User: read usernames from the file and store them into an ArrayList
    // @param: String
    //@return: Arraylist<String>
    public static ArrayList<String> Read_user(String filename) throws IOException {
        try {
            File users = new File("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\users.txt");
            users.createNewFile();
        } catch (IOException FileNotFoundException) {

        }
        FileReader fr = new FileReader("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\users.txt");
        BufferedReader in = new BufferedReader(fr);


        String line = in.readLine();

        int i = 0;
        ArrayList<String> userList;
        userList = new ArrayList<>();
        while (line != null && i < i + 1 && i < 25) {
            userList.add(in.readLine());
            //System.out.println(userList.get(i));
            i++;
        }
        in.close();
        fr.close();
        return userList;

    }
    // new_user: add the new username to the last line of the file
    // @param: String
    //@return: void
    public static void new_user(String filename) throws IOException {

        BufferedWriter output = new BufferedWriter(new FileWriter("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\users.txt", true));
        output.append("\n").append(user_name);
        output.close();
    }
    // check_user_file_exist: check if the previous user saved grid exists in the file
    // @param: String
    //@return: int[][]
    public static int[][] check_user_file_exist(String userfile) throws IOException {
        try {
            File users = new File(userfile);
            users.createNewFile();
        } catch (IOException FileNotFoundException) {

        }
        Scanner sc = new Scanner(System.in);
        FileReader uf = new FileReader(userfile);
        BufferedReader in = new BufferedReader(uf);
        String line = in.readLine();
        int i = 0;
        ArrayList<String> userGrid;
        userGrid = new ArrayList<>();
        while (line != null && i < i + 1 && i < 25) {
            if (line.equals(user_name)) {
                System.out.println(ANSI_GREEN+"Previous saved grid found. \n Would you like to contiune from previous saved grid? " +
                        "\n Yes to contiune, No if you want to start again"+ANSI_RESET);
                boolean flag_cont = false;
                while (!flag_cont) {
                    try {
                        String start_from_saved = sc.next();
                        if (!start_from_saved.equals("Yes") && !start_from_saved.equals("No")) {
                            Exception UserinputIsInvalid = new Exception();
                            throw UserinputIsInvalid;
                        } else if (start_from_saved.equals("No")) {
                            //sc.close();
                            Random_Tile();
                            score = 0;
                            flag_cont = true;
                            break;
                        } else {
                            //  sc.close();
                            flag_cont = true;
                            userline = i;
                            read_user_grid(i, grid, userfile);
                            score = read_user_score(grid,userfile);
                            break;
                        }
                    } catch (InputMismatchException e) {
                        sc.next();
                        System.out.println(ANSI_RED+"Please input a string"+ANSI_RESET);
                    } catch (Exception UserinputIsInvalid) {
                        System.out.println(ANSI_YELLOW+"Please input 'Yes' to load from saved game, 'No' to start from a new grid"+ANSI_RESET);
                    }
                }
                break;
            }
            i++;
        }
        uf.close();
        in.close();
        return grid;
    }
    // read_user_grid: read the saved user grid
    // @param: int, int[][], String
    //@return: void
    public static void read_user_grid(int i, int[][] grid, String userfile) throws IOException {
        FileReader uf = new FileReader(userfile);
        BufferedReader in = new BufferedReader(uf);
        String usergrid = " ";
        System.out.println("Loading saved game...");
        for (int j = 0; j < 2; j++) {
            usergrid = in.readLine();
        }
        //System.out.println(usergrid);
        String[] usrgrid = usergrid.split(" ");
        int count = 0;
        for (int l = 0; l < 4; l++) {

            for (int m = 0; m < 4; m++) {
                usrgrid[count] = usrgrid[count].trim();
                grid[l][m] = Integer.parseInt((usrgrid[count]));
                count++;
            }
            count++;
        }
        in.close();
        uf.close();
    }
    // read_user_score: read user saved score from the saved grid file
    // @param: int[][], String
    //@return: int
    public static int read_user_score(int[][]grid,String userfile) throws IOException {
        try {
            File users = new File(userfile);
            users.createNewFile();
        } catch (IOException FileNotFoundException) {

        }
        FileReader us = new FileReader(userfile);
        BufferedReader in = new BufferedReader(us);
        String userscore = " ";
        System.out.println("Loading score...");
        for (int j = 0; j < 3; j++) {
            userscore = in.readLine();
        }
        if(userscore != null){
            score = Integer.parseInt(userscore);
        }
        else{
            score = 0;
        }
        return score;
    }
    // left: move the grid to the left
    // @param: int[][]
    //@return: int[][]
    public static int[][] left(int[][] grid) {
        for (int x = 0; x < 4; x++) {
            for (int y = 1; y < 4 && y > -1; y++) {
                int i = x;
                int j = y;
                int a = 0;

                for (int z = 0; j > 0 && z == 0; j--) {
                    if (grid[i][j - 1] == tileint[0] && grid[i][j] != tileint[0]) {
                        grid[i][j - 1] = grid[i][j];
                        grid[i][j] = tileint[0];
                    } else if (grid[i][j - 1] == grid[i][j] && grid[i][j] != tileint[0]) {
                        a = Check_index(tileint, grid[i][j]);
                        score = score + tileint[a + 1];
                        grid[i][j - 1] = tileint[a + 1];
                        grid[i][j] = tileint[0];
                        z = 1;
                    } else if (grid[i][j - 1] != tileint[0] && grid[i][j] != grid[i][j - 1] && grid[i][j] != tileint[0]) {
                        z = 1;
                    } else if (grid[i][j] == tileint[0]) {
                        z = 1;
                    }
                }
            }
        }
        return grid;
    }
    // right: move the grid to the right
    // @param: int[][]
    //@return: int[][]
    public static int[][] right(int[][] grid) {
        for (int x = 0; x < 4; x++) {
            for (int y = 2; y < 3 && y > -1; y--) {
                int i = x;
                int j = y;
                int a = 0;
                //loop for check back
                for (int z = 0; j > -1 && z == 0 && j < 3; j++) {
                    if (grid[i][j + 1] == tileint[0] && grid[i][j] != tileint[0]) {
                        grid[i][j + 1] = grid[i][j];
                        grid[i][j] = tileint[0];
                    } else if (grid[i][j + 1] == grid[i][j] && grid[i][j] != tileint[0]) {
                        a = Check_index(tileint, grid[i][j]);
                        score = score + tileint[a + 1];
                        grid[i][j + 1] = tileint[a + 1];
                        grid[i][j] = tileint[0];
                        z = 1;
                    } else if (grid[i][j + 1] != tileint[0] && grid[i][j] != grid[i][j + 1] && grid[i][j] != tileint[0]) {
                        z = 1;
                    } else if (grid[i][j] == tileint[0]) {
                        z = 1;
                    }
                }
            }
        }
        return grid;
    }
    // up: move the grid upward
    // @param: int[][]
    //@return: int[][]
    public static int[][] up(int[][] grid) {
        for (int y = 0; y < 4; y++) {
            for (int x = 1; x < 4 && x > -1; x++) {
                int i = x;
                int j = y;
                int a = 0;
                //loop for check back
                for (int z = 0; i > 0 && z == 0; i--) {
                    if (grid[i - 1][j] == tileint[0] && grid[i][j] != tileint[0]) {
                        grid[i - 1][j] = grid[i][j];
                        grid[i][j] = tileint[0];
                    } else if (grid[i - 1][j] == grid[i][j] && grid[i][j] != tileint[0]) {
                        a = Check_index(tileint, grid[i][j]);
                        score = score + tileint[a + 1];
                        grid[i - 1][j] = tileint[a + 1];
                        grid[i][j] = tileint[0];
                        z = 1;
                    } else if (grid[i - 1][j] != tileint[0] && grid[i][j] != grid[i - 1][j] && grid[i][j] != tileint[0]) {
                        z = 1;
                    } else if (grid[i][j] == tileint[0]) {
                        z = 1;
                    }
                }
            }
        }
        return grid;
    }
    // down: move the grid downward
    // @param: int[][]
    //@return: int[][]
    public static int[][] down(int[][] grid) {
        for (int y = 3; y > -1; y--) {
            for (int x = 2; x > -1; x--) {
                int i = x;
                int j = y;
                int a = 0;
                //loop for check back
                for (int z = 0; i < 3 && z == 0; i++) {
                    if (grid[i + 1][j] == tileint[0] && grid[i][j] != tileint[0]) {
                        grid[i + 1][j] = grid[i][j];
                        grid[i][j] = tileint[0];
                    } else if (grid[i + 1][j] == grid[i][j] && grid[i][j] != tileint[0]) {
                        a = Check_index(tileint, grid[i][j]);
                        score = score + tileint[a + 1];
                        grid[i + 1][j] = tileint[a + 1];
                        grid[i][j] = tileint[0];
                        z = 1;
                    } else if (grid[i + 1][j] != tileint[0] && grid[i][j] != grid[i + 1][j] && grid[i][j] != tileint[0]) {
                        z = 1;
                    } else if (grid[i][j] == tileint[0]) {
                        z = 1;
                    }
                }
            }
        }
        return grid;
    }
    // Check_index: check if the index next to the corresponding index has the same value
    // @param: int[], int
    //@return: int
    public static int Check_index(int[] tileint, int element) {
        for (int i = 0; i < tileint.length; i++) {
            if (tileint[i] == element) {
                return i;
            }
        }
        return 0;
    }
    // move: move the grid according to user's choice
    // @param: int[][], int
    //@return: int[][]
    public static int[][] move(int[][] grid, int direction) {
        if (direction == 0) {
            grid = left(grid);

        } else if (direction == 1) {
            grid = down(grid);
        } else if (direction == 2) {
            grid = right(grid);
        } else if (direction == 3) {
            grid = up(grid);
        }
        return grid;
    }
    // Random_Tile: Randomly put a tile in the grid where it is empty
    // @param: none
    //@return: void
    public static void Random_Tile() {
        if (!check_Full(grid)) {
            Random it = new Random();
            Random jt = new Random();
            int i = it.nextInt(4);
            int j = jt.nextInt(4);
            while (grid[i][j] != 0) {
                i = it.nextInt(4);
                j = jt.nextInt(4);
            }
            int num = new Random().nextInt(2) + 4;
            if (num % 2 == 0) {
                grid[i][j] = 2;
            } else {
                grid[i][j] = 4;
            }
        } else {

        }
    }
    // check_Win : check if the user grid has 2048
    // @param: int[][]
    //@return: boolean
    public static boolean check_Win(int[][] grid) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == 2048) {
                    System.out.println("Congratulations! You have won the game!");
                    return true;
                }
            }
        }
        return false;
    }
    // check_Full: check if the user grid is full
    // @param: int[][]
    //@return: boolean
    public static boolean check_Full(int[][] grid) {
        boolean avaliable_space = true;
        boolean merge_col = true;
        boolean merge_row = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == 0) {
                    avaliable_space = false;
                    break;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == grid[i][j + 1]) {
                    merge_col = false;
                    break;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == grid[i + 1][j]) {
                    merge_row = false;
                    break;
                }
            }
        }
        if (avaliable_space && merge_col && merge_row == true) {
            return true;
        }
        return false;
    }
    // print_grid: print the current grid
    // @param: int[][]
    //@return: void
    public static void print_grid(int[][] grid) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] > 10 && grid[i][j] < 100) {
                    System.out.print("  " + grid[i][j] + "   ");
                } else if (grid[i][j] > 100 && grid[i][j] < 1000) {
                    System.out.print(" " + grid[i][j] + "   ");
                } else if (grid[i][j] > 1000) {
                    System.out.print(grid[i][j] + "   ");
                } else {
                    System.out.print("   " + grid[i][j] + "   ");
                }
            }
            System.out.println();
        }
    }
    // Direction: return a numerical value from the user input
    // @param: String
    //@return: int
    public static int Direction(String key) {
        switch (key) {
            case "A": {
                return 0;
            }
            case "S": {
                return 1;
            }
            case "D": {
                return 2;
            }
            case "W": {
                return 3;
            }
            case "X": {
                return 4;
            }
            default:
                return 4;
        }
    }
    // startGame: Start the 2048 game
    // @param: int[][], String
    //@return: void
    public static void startGame(int[][] grid, String userfile) throws IOException {
        Scanner key = new Scanner(System.in);
        System.out.println("Welcome to the 2048 game with file.io \n Please select a color for the gameboard: \n " +
                "0 for default, 1 for Cyan");
        boolean flag_cont = false;
        while (!flag_cont) {
            try {
                int color = key.nextInt();
                if (color!=0 && color!=1) {
                    Exception UserinputIsInvalid = new Exception();
                    throw UserinputIsInvalid;
                } else if (color==0) {
                    flag_cont = true;
                    System.out.println("Default Color");
                } else {
                    flag_cont = true;
                   System.out.println(ANSI_CYAN+"Cyan Mode");
                }
            } catch (InputMismatchException e) {
                key.next();
                System.out.println(ANSI_RED+"Please input an integer"+ANSI_RESET);
            } catch (Exception UserinputIsInvalid) {
                System.out.println(ANSI_YELLOW+"Please input 0 for default, 1 for Cyan"+ANSI_RESET);
            }
        }
        String user_key = "X";
        do {
            print_grid(grid);
            System.out.println("Current Score:" + score);
            System.out.println("Use A for left, W for up, S for down and D for right; press X to quit the game");
            boolean flag = false;
            while (!flag) {
                if (key.hasNext()) {
                    try {
                        user_key = key.next().toUpperCase();

                        if (user_key.equals(null) || !user_key.equals("A") && !user_key.equals("W") && !user_key.equals("S") && !user_key.equals("D") && !user_key.equals("X")) {
                            Exception userinputisinvalid = new Exception();
                            throw userinputisinvalid;
                        }
                        flag = true;
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Please input a string");
                    } catch (Exception userinputisinvalid) {
                        System.out.println("Use A for left, W for up, S for down and D for right; press X to quit the game");
                    }
                }
            }
            int direction = Direction(user_key);
            grid = move(grid, direction);
            Random_Tile();
            check_Win(grid);
            if (check_Full(grid) || direction == 4) {
                check_Win(grid);
                System.out.println(ANSI_RESET+"Game Over :(");
                System.out.println("Score :" + score);
                save_user_grid( grid, userfile,score);
                save_score(score);
                ArrayList<Users> Leaderboard = read_leaderboard();
                write_leaderboard(Leaderboard);
                System.out.println("Display the leaderboard?");
                boolean flag_dis = false;
                while (!flag_dis) {
                    try {
                        String display = key.next();
                        if (!display.equals("Yes") && !display.equals("No")) {
                            Exception UserinputIsInvalid = new Exception();
                            throw UserinputIsInvalid;
                        } else if (display.equals("No")) {
                            flag_dis = true;
                        } else {
                            flag_dis = true;
                            display_Leaderboard();
                        }
                    } catch (InputMismatchException e) {
                        key.next();
                        System.out.println(ANSI_RED+"Please input a String"+ANSI_RESET);
                    } catch (Exception UserinputIsInvalid) {
                        System.out.println(ANSI_YELLOW+"Please input 'Yes' to display the leaderboard, 'No' to skip this function"+ANSI_RESET);
                    }
                }
                break;
            }

        } while (!check_Full(grid));

    }
    // save_score: save the user's username and score to the leaderboard
    // @param: int
    //@return: void
    public static void save_score(int score) throws IOException {
        String user_score = Integer.toString(score);
        BufferedWriter output = new BufferedWriter(new FileWriter("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\scores.txt", true));
        output.append(user_name).append(" ").append(user_score);
        output.close();
    }
    // save_user_grid: save the user's grid, username, grid and score
    // @param: int[][], String, int
    //@return:void
    public static void save_user_grid(int[][] grid, String userfile, int score) throws IOException {
        FileWriter uf = new FileWriter(userfile);
        BufferedWriter in = new BufferedWriter(uf);

        System.out.println("Saving game...");

        //System.out.println(usergrid);
        String[] usrgrid = new String[16];
        int count = 0;
        StringBuffer sb = new StringBuffer();
        for (int l = 0; l < 4; l++) {

            for (int m = 0; m < 4; m++) {
                if (count < 16) {
                    sb.append(grid[l][m] + " ");
                    count++;
                }

            }
            count++;
        }
        String usergrid = sb.toString();
        String userscore = Integer.toString(score);
        in.write(user_name);
        in.newLine();
        in.write(usergrid);
        in.newLine();
        in.write(userscore);
        in.close();
        uf.close();
    }
    // read_leaderboard: read the leaderboard for sorting
    // @param: none
    //@return: ArrayList<Users>
   public static ArrayList<Users> read_leaderboard() throws IOException {
       FileReader fr = new FileReader("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\scores.txt");
       BufferedReader in = new BufferedReader(fr);
       ArrayList<Users> Leaderboard = new ArrayList<Users>();
       String currentLine = in.readLine();
        int usrscores = 0;
       String name = " ";
       while (currentLine != null)
       {
           String[] UsrDetail = currentLine.split(" ");

            name = UsrDetail[0];

           usrscores = Integer.valueOf(UsrDetail[1]);

           //Creating user object for every user and adding it to ArrayList

           Leaderboard.add(new Users(name, usrscores));

           currentLine = in.readLine();
       }

       //Sorting ArrayList based on user scores

       Collections.sort(Leaderboard, new scoresCompare());
       fr.close();
        return Leaderboard;
   }
    // write_leaderboard: write the sorted leaderboard to the file
    // @param: ArrayList<Users>
    //@return: void
   public static void write_leaderboard(ArrayList<Users> Leaderboard) throws IOException {
       BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\scores.txt"));


       for (Users user : Leaderboard)
       {
           writer.write(user.usernames);

           writer.write(" "+user.userscores);

           writer.newLine();
       }
       writer.close();
   }
    // display_Leaderboard: display the sorted leaderboard
    // @param: none
    //@return:  void
    public static void display_Leaderboard() throws IOException {
        FileReader fr = new FileReader("C:\\Users\\Leonard\\IdeaProjects\\2048\\src\\com\\company\\scores.txt");
        BufferedReader in = new BufferedReader(fr);
        String line = in.readLine();
        int i=0;
        while(line!=null){
            if(i==0){
                System.out.println(ANSI_YELLOW+(i+1)+": "+line+ANSI_RESET);
            }
            else if(i==1){
                System.out.println(ANSI_CYAN+(i+1)+": "+line+ANSI_RESET);

            }
            else if(i==2){
                System.out.println(ANSI_GREEN+(i+1)+": "+line+ANSI_RESET);

            }else{
                System.out.println((i+1)+": "+line);
            }
            line = in.readLine();
            i++;
            }
        }
    // contiune: ask the user to contiune the game
    // @param: none
    //@return: boolean
        public static boolean contiune(){
        Scanner sc = new Scanner(System.in);
         System.out.println("Contiune? Yes to contiune, No to end the game");
            boolean flag_cont = false;
            while (!flag_cont) {
                try {
                    String start_again = sc.next();
                    if (!start_again.equals("Yes") && !start_again.equals("No")) {
                        Exception UserinputIsInvalid = new Exception();
                        throw UserinputIsInvalid;
                    } else if (start_again.equals("No")) {
                        flag_cont = true;
                       System.out.println(ANSI_GREEN+"Thank you for playing 2048 :)"+ANSI_RESET);
                       return false;
                    } else {
                        flag_cont = true;
                       return true;
                    }
                } catch (InputMismatchException e) {
                    sc.next();
                    System.out.println(ANSI_RED+"Please input a string"+ANSI_RESET);
                } catch (Exception UserinputIsInvalid) {
                    System.out.println(ANSI_YELLOW+"Please input 'Yes' to play again, 'No' to exit the game"+ANSI_RESET);
                }
            }
            return false;
        }
    }
    //for sorting the leaderboard
    // https://javaconceptoftheday.com/how-to-sort-a-text-file-in-java/
class Users
{
    String usernames;

    int userscores;

    public Users(String usernames, int userscores)
    {
        this.usernames = usernames;

        this.userscores = userscores;
    }
}

//nameCompare Class to compare the names
class nameCompare implements Comparator<Users>
{
    @Override
    public int compare(Users s1, Users s2)
    {
        return s1.usernames.compareTo(s2.usernames);
    }
}

//marksCompare Class to compare the scores

class scoresCompare implements Comparator<Users>
{
    @Override
    public int compare(Users s1, Users s2)
    {
        return s2.userscores - s1.userscores;
    }
}