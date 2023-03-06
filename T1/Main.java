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

            Belt pairs = new Belt(productsFirst, productsSecond);
            pairs.setProducts(1, firstBelt);
            pairs.setProducts(2, secondBelt);

            pairs.beltPairs();
        }
    }
}

class Belt {
    // first belt
    int productsFirst;
    String[] typeFirst;
    int[] valueFirst;
    
    // second belt
    int productsSecond;
    String[] typeSecond;
    int[] valueSecond;

    long[][] maxValues;
    int[][] minPairs;

    public Belt(int productsFirst, int productsSecond) {
        this.productsFirst = productsFirst;
        typeFirst = new String[productsFirst];
        valueFirst = new int[productsFirst];

        this.productsSecond = productsSecond;
        typeSecond = new String[productsSecond];
        valueSecond = new int[productsSecond];

        maxValues = new long[productsFirst + 1][productsSecond + 1];
        minPairs = new int[productsFirst + 1][productsSecond + 1];
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

    // FIX mooshak dá wrong answer
    public Result beltPairs() {
        for(int i = 0; i <= productsFirst; i++) {
            minPairs[i][0] = 0;
            maxValues[i][0] = 0;
        }

        for(int j = 0; j <= productsSecond; j++) {
            minPairs[0][j] = 0;
            maxValues[0][j] = 0;
        }

        for(int l = 1; l <=productsFirst; l++) {
            for(int c = 1; c <= productsSecond; c++) {

                if(typeFirst[l-1].equals(typeSecond[c-1])) {
                    maxValues[l][c] = valueFirst[l-1] + valueSecond[c-1] + maxValues[l-1][c-1];
                    minPairs[l][c] = 1 + minPairs[l-1][c-1];
                } else if(maxValues[l-1][c] >= maxValues[l][c-1]) {
                    maxValues[l][c] = maxValues[l-1][c];

                    if(minPairs[l-1][c] != 0 && minPairs[l][c-1] != 0) {
                        if(minPairs[l-1][c] <= minPairs[l][c-1]) {
                            minPairs[l][c] = minPairs[l-1][c];
                        } else {
                            minPairs[l][c] = minPairs[l-1][c];
                        }
                    } else {
                        if(minPairs[l-1][c] != 0 && minPairs[l][c-1] == 0) {
                            minPairs[l][c] = minPairs[l-1][c];
                        } else {
                            minPairs[l][c] = minPairs[l][c-1];
                        }
                    }
                } else {
                    maxValues[l][c] = maxValues[l][c-1];

                    if(minPairs[l-1][c] != 0 && minPairs[l][c-1] != 0) {
                        if(minPairs[l-1][c] <= minPairs[l][c-1]) {
                            minPairs[l][c] = minPairs[l-1][c];
                        } else {
                            minPairs[l][c] = minPairs[l-1][c];
                        }
                    } else {
                        if(minPairs[l-1][c] != 0 && minPairs[l][c-1] == 0) {
                            minPairs[l][c] = minPairs[l-1][c];
                        } else {
                            minPairs[l][c] = minPairs[l][c-1];
                        }
                    }
                }
            }
        }

        return new Result(maxValues[productsFirst][productsSecond], minPairs[productsFirst][productsSecond]);
    }
}

class Result {
    public Result(long maxValue, int minPair) {
        System.out.println(maxValue + " " + minPair);
    }
}