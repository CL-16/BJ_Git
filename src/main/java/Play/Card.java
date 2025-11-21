package Play;

public class Card
{
    private String suit;
    private String number;
    private int value;


    public Card(String suit, String number, int value)
    {
        this.suit = suit;
        this.number = number;
        this.value = value;
    }

    public String get_suit()
    {
        return suit;
    }

    public String get_number()
    {
        return number;
    }

    public int get_value()
    {
        return value;
    }

    public String toString()
    {
        return number + " of " + suit;
    }

}