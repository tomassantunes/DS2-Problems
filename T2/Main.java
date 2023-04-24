import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
       
        String[] tmp = input.readLine().split(" ");

        int R = Integer.parseInt(tmp[0]); // rows
        int C = Integer.parseInt(tmp[1]); // columns
        int T = Integer.parseInt(tmp[2]); // test cases

        char[][] map = new char[R][C];
       
        for(int i = 0; i < R; i++) {
            map[i] = input.readLine().toCharArray();
        }

        Dream dream = new Dream(R, C, map);

        for(; T > 0; T--) {
            tmp = input.readLine().split(" ");

            int steps = dream.escape(Integer.parseInt(tmp[0]) - 1, Integer.parseInt(tmp[1]) - 1);

            if(steps == -1)
                System.out.println("Stuck");
            else
                System.out.println(steps);
        }
    }
}

class Dream {
    int rows;
    int columns;
    int nodes;
    char[][] map;
    List<Point>[] adj;

    int[][] pointMap;
    int c = 1;

    Point hole;

    @SuppressWarnings("unchecked")
    public Dream(int rows, int columns, char[][] map) {
        this.rows = rows;
        this.columns = columns;
        this.nodes = rows * columns;
        this.map = map;

        pointMap = new int[rows][columns];

        adj = new List[nodes];
        for(int i = 0; i < nodes; i++) {
            adj[i] = new LinkedList<>();
        }

        buildGraph();
    }

    public int getPoint(Point u) {
        if(pointMap[u.x][u.y] == 0) {
            pointMap[u.x][u.y] = c;
            c++;
        }

        return pointMap[u.x][u.y] - 1;
    }

    public void addEdge(Point u, Point v) {
        if(v.x == -1 && v.y == -1) return;

        if(map[v.x][v.y] == 'H') {
            hole = v;
        }

        adj[getPoint(u)].add(v);
    }

    public Point buildArch(int x, int y, int v1, int v2) {
        int xi = x;
        int yi = y;

        while(x+v1 >= 0 && x+v1 < rows && y+v2 >= 0 && y+v2 < columns) {
            char tmp = map[x+v1][y+v2];

            if(tmp == '.') {
                x += v1;
                y += v2;
            } else if(tmp == 'O') {
                if(x == xi && y == yi) break;
                return new Point(x, y);
            } else if(tmp == 'H') {
                return new Point(x+v1, y+v2);
            }
       }

        return new Point(NONE, NONE);        
    }

    public void buildGraph() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(map[i][j] == '.') {
                    addEdge(new Point(i, j), buildArch(i, j, 1, 0));
                    addEdge(new Point(i, j), buildArch(i, j, -1, 0));
                    addEdge(new Point(i, j), buildArch(i, j, 0, 1));
                    addEdge(new Point(i, j), buildArch(i, j, 0, -1));
                }
            }
        }
    }

    public static final int INFINITY = Integer.MAX_VALUE;
    public static final int NONE = -1;

    public int escape(int sx, int sy) {
        int s = getPoint(new Point(sx, sy));
        int[] color  = new int[nodes];
        int[] d = new int[nodes];

        for(int u = 0; u < nodes; u++) {
            color[u] = 0;
            d[u] = INFINITY;
        }

        color[s] = 1;
        d[s] = 0;

        Queue<Integer> Q = new LinkedList<>();
        Q.add(s);

        while(!Q.isEmpty()) {
            int u = Q.remove();

            for(Point v : adj[u]) {
                int vi = getPoint(v);
                if(color[vi] == 0) {
                    color[vi] = 1;
                    d[vi] = d[u] + 1;

                    if(!v.equals(hole))
                        Q.add(vi);
                }
            }
        }

        int dHole = d[getPoint(hole)];
        if(dHole == Integer.MAX_VALUE)
            return -1;
        return dHole;
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