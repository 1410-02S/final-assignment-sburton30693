class Food extends Position {
    public String name = "example food";
    private int value = 5;
    private int life = 5;

    public Food() { }
    public Food(String name, int value, int life, int x, int y) {
        super(x, y);
        this.name = name;
        this.value = value;
        this.life = life;
    }

    public int getValue() {
        return value;
    }

    public void update() {
        life--;
    }

    public boolean rotten() {
        return life <= 0;
    }
}