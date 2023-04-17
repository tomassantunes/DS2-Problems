import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.Pipe;
import java.util.LinkedList;
import java.util.List;

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
       
        String[] tmp = input.readLine().split(" ");

        int R = Integer.parseInt(tmp[0]); // rows
        int C = Integer.parseInt(tmp[1]); // columns
        int T = Integer.parseInt(tmp[2]); // test cases

        String[][] map = new String[R][C];
       
        int nodes = 0;
        for(int i = 0; i < R; i++) {
            tmp = input.readLine().split("");
            for(int j = 0; j < C; j++) {
                map[i][j] = tmp[j];
                if(map[i][j] == ".") nodes++;
            }
        }

        Dream dream = new Dream(R, C, nodes, map);

        for(; T > 0; T--) {
            tmp = input.readLine().split(" ");
            dream.escape(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
        }
    }
}

class Dream {
    int rows;
    int columns;
    int nodes;
    String[][] map;
    List<Point>[] adj;

    int[][] pointMap;
    int c = 1;

    @SuppressWarnings("unchecked")
    public Dream(int rows, int columns, int nodes, String[][] map) {
        this.rows = rows;
        this.columns = columns;
        this.map = map;

        pointMap = new int[rows][columns];

        adj = new List[nodes];
        for(int i = 0; i < nodes; i++) {
            adj[i] = new LinkedList<>();
        }

        buildGraph();
        printGraph();
    }

    public int getPoint(Point u) {
        if(pointMap[u.x][u.y] == 0) {
            pointMap[u.x][u.y] = c;
            c++;
        }
         
        return pointMap[u.x][u.y];
    }

    public void addEdge(Point u, Point v) {
        if(map[v.x][v.y] == "O") return;
        adj[getPoint(u)].add(v);
    } 

    public void printGraph() {
        for(var x : adj) {
            for(var y : x) {
                System.out.print(y + " ");
            }
            System.out.println("");
        }
    }

    public void buildGraph() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(map[i][j] == ".") {
                    System.out.println(i + " " + j);
                    Point u = new Point(i, j);

                    if(i > 0 && i < rows-1 && j > 0 && j < columns-1) {
                        addEdge(u, new Point(i-1, j));
                        addEdge(u, new Point(i+1, j));
                        addEdge(u, new Point(i, j-1));
                        addEdge(u, new Point(i, j+1));
                    }

                    if(i == 0) {
                        if(j == 0) {
                            addEdge(u, new Point(i, j+1));
                            addEdge(u, new Point(i+1, j));
                        } else if(j == columns-1) {
                            addEdge(u, new Point(i, j-1));
                            addEdge(u, new Point(i+1, j));
                        } else {
                            addEdge(u, new Point(i, j-1));
                            addEdge(u, new Point(i, j+1));
                            addEdge(u, new Point(i+1, j));
                        }
                    }

                    if(i == rows-1) {
                        if(j == 0) {
                            addEdge(u, new Point(i, j+1));
                            addEdge(u, new Point(i-1, j));
                        } else if(j == columns-1) {
                            addEdge(u, new Point(i, j-1));
                            addEdge(u, new Point(i-1, j));
                        } else {
                            addEdge(u, new Point(i, j-1));
                            addEdge(u, new Point(i, j+1));
                            addEdge(u, new Point(i-1, j));
                        }
                    }
                }
            }
        }
    }

    public int escape(int xi, int yi) {
        return 0;
    }
}

class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}