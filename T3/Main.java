import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

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
    }
}

class Alertland {
    int R;
    int[] p; // population size of each region
    int[] d; // departure capacity of each region
    List<Integer>[] rails;

    @SuppressWarnings("unchecked")
    public Alertland(int R) {
        this.R = R;

        this.p = new int[R];
        this.d = new int[R];

        this.rails = new List[R];
        for(int i = 0; i < R; i++) {
            this.rails[i] = new LinkedList<>();
        }
    }

    public void addRegion(int i, int pop, int dep) {
        p[i] = pop;
        d[i] = dep;
    }

    public void addRail(int r1, int r2) {
        rails[r1-1].add(r2-1);
        rails[r2-1].add(r1-1);
    }
}

// class Rail {
//     public int d;
//     public int c;
//     public int f;

//     public Rail(int destination, int capacity) {
//         this.d = destination;
//         this.c = capacity;

//         f = 0;
//     }
// }