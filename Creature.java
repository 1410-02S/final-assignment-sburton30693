class Creature extends Position {
    public String name = "example creature";
    private int hunger = 0;
    private int health = 10;

    public Creature() { }
    public Creature(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public void display() {
        System.out.println(name + " | Health: " + health + " | Hunger: " + hunger);
    }

    public boolean dead() {
        return health <= 0;
    }
    
    public void idle(int hungerDiff) {
        if(hunger > health) {
            health -= Math.ceil((float)Math.abs(health - hunger) / 2.0f);
        }
        
        hunger += hungerDiff;
    }

    public void eat(Food food) {
        hunger = Math.min(hunger - food.getValue(), 0);
    }

    public void move(int xOff, int yOff) {
        idle((int)Math.sqrt(xOff * xOff + yOff * yOff));
        x += xOff;
        y += yOff;
    }

    public Creature reproduce() {
        idle(2);

        String offspringName;

        if(name.indexOf(' ') != -1) {
            int generation = Integer.parseInt(name.substring(name.indexOf(' ') + 1), 10);
            offspringName = name.substring(0, name.indexOf(' ')) + " " + (generation + 1);
        } else {
            offspringName = name + " 2";
        }

        Creature offspring = new Creature(offspringName, x, y);

        return offspring;
    }
}