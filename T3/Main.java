import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Main {
    public static void main(String[] args) throws IOException, NumberFormatException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String[] tmp = input.readLine().split(" ");
        int R = Integer.parseInt(tmp[0]);
        int L = Integer.parseInt(tmp[1]);

        Alertland alertland = new Alertland(R);

        for(int i = 0; i < R; i++) {
            tmp = input.readLine().split(" ");
            alertland.addRegion(i, Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
        }

        for(; L > 0; L--) {
            tmp = input.readLine().split(" ");
            alertland.addRail(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
        }

        int safe = Integer.parseInt(input.readLine());
        System.out.println(alertland.edmondsKarp(safe));
    }
}

class Alertland {
    int R;
    int nodes;
    int source;
    List<Rail>[] rails;

    private static final int INFINITY = Integer.MAX_VALUE;
    private static final int NONE = -1;

    @SuppressWarnings("unchecked")
    public Alertland(int R) {
        this.R = R;
        this.nodes = (R*2) + 1;
        this.source = 0;

        this.rails = new List[nodes];
        for(int i = 0; i < nodes; i++) {
            this.rails[i] = new LinkedList<>();
        }
    }

    public void addRegion(int i, int pop, int dep) {
        rails[source].add(new Rail(i+1, pop));
        rails[i+1].add(new Rail(i+1+R, dep));
    }


    public void addRail(int r1, int r2) {
        rails[r1+R].add(new Rail(r2, INFINITY));
        rails[r2+R].add(new Rail(r1, INFINITY));
    }

    public void addRail(int r1, int r2, int c) {
        rails[r1].add(new Rail(r2, c));
    }

    // public void printRails() {
    //     for(int i = 0; i < nodes; i++) {
    //         System.out.print(i + " ");
    //         for(var x : rails[i]) {
    //             System.out.print(x.d + " " + x.c + " ");
    //         }
    //         System.out.println();
    //     }
    // }

    private Alertland buildResidualNetwork() {
        Alertland r = new Alertland(R);

        for(int i = 0; i < nodes; i++) {
            for(Rail e : rails[i]) {
                int destination = e.d;
                int capacity = e.c;
                int flow = e.f;

                r.addRail(i, destination, capacity - flow);
                r.addRail(i, destination, flow);
            }
        }

        return r;
    }

    private void updateResidualCapacity(int from, int to, int capacity, int flow) {
        for(Rail e : rails[from]) {
            if(e.d == to) {
                e.c = capacity - flow;
                break;
            }
        }

        for(Rail e : rails[to]) {
            if(e.d == from) {
                e.c = flow;
                break;
            }
        }
    }

    private void incrementFlow(int[] p, int increment, Alertland r, int sink) {
        int v = sink;
        int u = p[v];

        while(u != NONE) {
            int uv = 0;

            for(Rail e : rails[u]) {
                if(e.d == v) {
                    e.f += increment;
                    r.updateResidualCapacity(u, v, e.c, e.f);
                    uv = 1;
                    break;
                }
            }

            if(uv == 0) {
                for(Rail e : rails[v]) {
                    if(e.d == u) {
                        e.f -= increment;
                        r.updateResidualCapacity(v, u, e.c, e.f);
                        break;
                    }
                }
            }

            v = u;
            u = p[v];
        }
    }

    private int findPath(int[] p, int sink) {
        int[] cf = new int[nodes];

        Queue<Integer> Q = new LinkedList<>();

        for(int u = 0; u < nodes; u++) {
            p[u] = NONE;
        }

        cf[source] = INFINITY;
        Q.add(source);

        while(!Q.isEmpty()) {
            int u = Q.remove();

            if(u == sink) break;

            for(Rail e : rails[u]) {
                int v = e.d;

                if(e.c > 0 && cf[v] == 0) {
                    cf[v] = Math.min(cf[u], e.c);

                    p[v] = u;
                    Q.add(v);
                }
            }
        }

        return cf[sink];
    }

    public int edmondsKarp(int sink) {
        Alertland r = buildResidualNetwork();
        int flowValue = 0;
        int[] p = new int[nodes];
        int increment;

        while((increment = r.findPath(p, sink)) > 0) {
            incrementFlow(p, increment, r, sink);
            flowValue += increment;
        }

        return flowValue;
    }
}

class Rail {
    public int d;
    public int c;
    public int f;

    public Rail(int destination, int capacity) {
        this.d = destination;
        this.c = capacity;

        f = 0;
    }
}