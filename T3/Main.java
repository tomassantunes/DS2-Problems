import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
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
    int source;
    int[] pop; // population size of each region
    int[] dep; // departure capacity of each region
    List<Rail>[] rails;

    private static final int INFINITY = Integer.MAX_VALUE;
    private static final int NONE = -1;

    @SuppressWarnings("unchecked")
    public Alertland(int R) {
        this.R = R+1;
        this.source = 0;

        this.pop = new int[this.R];
        this.dep = new int[this.R];

        this.rails = new List[this.R];
        for(int i = 0; i < this.R; i++) {
            this.rails[i] = new LinkedList<>();
        }
    }

    public void addRegion(int i, int pop, int dep) {
        this.pop[i+1] = pop;
        this.dep[i+1] = dep;
        rails[source].add(new Rail(i+1, pop));
    }


    public void addRail(int r1, int r2) {
        rails[r1].add(new Rail(r2, INFINITY));
        rails[r2].add(new Rail(r1, INFINITY));
    }

    public void printDep() {
        for(var x : dep) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    public void printRails() {
        for(int i = 0; i < R; i++) {
            System.out.print(i + " ");
            for(var x : rails[i]) {
                System.out.print(x.d + " ");
            }
            System.out.println();
        }
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
                dep[from] -= flow;
                break;
            }
        }
    }

    private void incrementFlow(int[] p, int increment, int sink) {
        int v = sink;
        int u = p[v];

        while(u != NONE) {
            int uv = 0;

            for(Rail e : rails[u]) {
                if(e.d == v) {
                    e.f += increment;
                    updateResidualCapacity(u, v, e.c, e.f);
                    uv = 1;
                    break;
                }
            }

            if(uv == 0) {
                for(Rail e : rails[v]) {
                    if(e.d == u) {
                        e.f -= increment;
                        updateResidualCapacity(v, u, e.c, e.f);
                        break;
                    }
                }
            }

            v = u;
            u = p[v];
        }
    }

    private int findPath(int[] p, int sink) {
        int[] cf = new int[R];

        Queue<Integer> Q = new LinkedList<>();

        for(int u = 0; u < R; u++) {
            p[u] = NONE;
        }

        cf[source] = INFINITY;
        Q.add(source);

        while(!Q.isEmpty()) {
            int u = Q.remove();

            if(u == sink) break;

            for(Rail e : rails[u]) {
                int v = e.d;

                if(e.c > 0 && cf[v] == 0 && dep[v] > 0) {
                    // cf[v] = Math.min(cf[u], e.c);

                    if(cf[u] <= dep[v]) {
                        cf[v] = cf[u];
                    } else {
                        cf[v] = dep[v];
                    }

                    p[v] = u;
                    Q.add(v);
                }

                printDep();
            }
        }

        return cf[sink];
    }

    public int edmondsKarp(int sink) {
        int flowValue = 0;
        int[] p = new int[R];
        int increment;

        while((increment = findPath(p, sink)) > 0) {
            incrementFlow(p, increment, sink);
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