import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
       
        String[] tmp = input.readLine().split(" ");

        int R = Integer.parseInt(tmp[0]); // rows
        int C = Integer.parseInt(tmp[1]); // columns
        int T = Integer.parseInt(tmp[2]); // test cases

        String[][] map = new String[R][C];
       
        for(int i = 0; i < R; i++) {
            tmp = input.readLine().split("");
            for(int j = 0; j < C; j++) {
                map[i][j] = tmp[j];
            }
        }

        Dream dream = new Dream(R, C, map);

        for(; T > 0; T--) {
            tmp = input.readLine().split(" ");
            dream.escape(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
        }
    }
}

class Dream {
    int rows;
    int columns;
    String[][] map;

    public Dream(int rows, int columns, String[][] map) {
        this.rows = rows;
        this.columns = columns;
        this.map = map;
    }

    public int escape(int xi, int yi) {
        return 0;
    }
}