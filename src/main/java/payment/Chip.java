package payment;

public enum Chip {
    RED(100),
    GREEN(25),
    BLUE(10),
    BLACK(500);

    private final int value;

    Chip(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}