import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int cases = Integer.parseInt(input.readLine());

        for(; cases > 0; cases--) {
            // first belt
            int productsFirst = Integer.parseInt(input.readLine());
            String[] firstBelt = new String[productsFirst];

            for(int i = 0; i < productsFirst; i++)
                firstBelt[i] = input.readLine();

            // second belt
            int productsSecond = Integer.parseInt(input.readLine());
            String[] secondBelt = new String[productsSecond];

            for(int j = 0; j < productsSecond; j++)
                secondBelt[j] = input.readLine();

            Pairs pairs = new Pairs(productsFirst, productsSecond);
            pairs.setProducts(1, firstBelt);
            pairs.setProducts(2, secondBelt);
        }
    }
}

class Pairs {
    // first belt
    int productsFirst;
    String[] typeFirst;
    int[] valueFirst;
    
    // second belt
    int productsSecond;
    String[] typeSecond;
    int[] valueSecond;

    long[][] maxValue;
    int[][] minPairs;

    public Pairs(int productsFirst, int productsSecond) {
        this.productsFirst = productsFirst;
        typeFirst = new String[productsFirst];
        valueFirst = new int[productsFirst];

        this.productsSecond = productsSecond;
        typeSecond = new String[productsSecond];
        valueSecond = new int[productsSecond];

        maxValue = new long[productsFirst][productsSecond];
        minPairs = new int[productsFirst][productsSecond];
    }

    public void setProducts(int belt, String[] products) {
        if(belt == 1) { // first belt
            for(int i = 0; i < productsFirst; i++) {
                String[] product = products[i].split(" ");
                typeFirst[i] = product[1];
                valueFirst[i] = Integer.parseInt(product[2]);
            }
        } else { // second belt
            for(int i = 0; i < productsSecond; i++) {
                String[] product = products[i].split(" ");
                typeSecond[i] = product[1];
                valueSecond[i] = Integer.parseInt(product[2]);
            }
        }
    }
}