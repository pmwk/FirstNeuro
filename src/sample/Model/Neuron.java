package sample.Model;

public class Neuron {
    final static int SIZE = 30;

    private int weights[][] = new int[SIZE][SIZE];

    private char value;

    public Neuron (char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public int getWeight(int data[][]) {
        if (data.length != SIZE || data[0].length != SIZE) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int weight = 0;

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                weight += data[i][j] * weights[i][j];
            }
        }

        return weight;
    }

    private int history[][] = new int[SIZE][SIZE];

    public void training (int data[][], boolean result) {

    }
}
