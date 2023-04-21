class Position {
    protected int x = 0;
    protected int y = 0;

    public Position() { }
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getPosition() {
        return new int[] {x, y};
    }
}