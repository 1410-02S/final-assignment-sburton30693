import java.util.Random;
import java.util.ArrayList;

class World {
    private static Random random = new Random(System.currentTimeMillis());
    private int time = 0;
    private int newCreatures = 0;
    private int deadCreatures = 0;
    private int newFood = 0;
    private int rottenFood = 0;
    private ArrayList<Creature> creatures = new ArrayList<>();
    private ArrayList<Food> food = new ArrayList<>();

    public World(int numCreatures, int numFood) {
        for(int i = 0; i < numCreatures; i++) {
            spawnCreature();
        }

        for(int i = 0; i < numFood; i++) {
            spawnFood();
        }
    }

    public void display() {
        System.out.println("Time: " + time + " | Creatures: " + creatures.size() + " | Food: " + food.size());
        System.out.println("Creatures: New: " + newCreatures + " Dead: " + deadCreatures + " | Food: New: " + newFood + " Rotten: " + rottenFood);
    }

    public void displayCreatures() {
        for(int i = 0; i < creatures.size(); i++) {
            creatures.get(i).display();
        }
    }

    public void step() {
        time++;
        newCreatures = 0;
        deadCreatures = 0;
        newFood = 0;
        rottenFood = 0;

        if(random.nextDouble() < 0.1) {
            spawnCreature();
        }

        for(int i = 0; i < creatures.size(); i++) {
            if(random.nextDouble() < 0.2) {
                spawnFood();
            }
        }

        for(int i = food.size() - 1; i >= 0; i--) {
            food.get(i).update();

            if(food.get(i).rotten()) {
                System.out.println(food.get(i).name + " rotted");
                food.remove(i);
                rottenFood++;
            }
        }

        for(int i = creatures.size() - 1; i >= 0; i--) {
            int action = 0;

            //Each succeeding action (move, eat, reproduce) is less likely than the one before
            double[] probabilites = {0.4, 0.2};
            for(int j = 0; j < 2; j++) {
                if(random.nextDouble() < probabilites[j]) {
                    action++;
                }
            }
            
            switch(action) {
                case 0 : //Move
                    int closestFood = -1;
                    int xMove = 0, yMove = 0;

                    for(int f = 0; f < food.size(); f++) {
                        int[] pos = food.get(f).getPosition();
                        int xDiff = pos[0] - creatures.get(i).getPosition()[0];
                        int yDiff = pos[1] - creatures.get(i).getPosition()[1];
                        
                        if(Math.sqrt(xDiff * xDiff + yDiff * yDiff) < 7) {
                            closestFood = f;
                            xMove = Math.max(-2, Math.min(xDiff, 2));
                            yMove = Math.max(-2, Math.min(yDiff, 2));
                            break;
                        }
                    }

                    //If no food nearby, move randomly
                    if(closestFood == -1) {
                        xMove = random.nextInt(-2, 3);
                        yMove = random.nextInt(-2, 3);
                    }
                
                    creatures.get(i).move(xMove, yMove);
                    System.out.println(creatures.get(i).name + " moved " + xMove + " " + yMove);
                    break;
                case 1 : //Eat
                    int foodInTile = -1;
                    int[] creaturePos = creatures.get(i).getPosition();

                    for(int f = 0; f < food.size(); f++) {
                        int[] pos = food.get(f).getPosition();

                        if(pos[0] == creaturePos[0] && pos[1] == creaturePos[1]) {
                            foodInTile = f;
                            break;
                        }
                    }

                    if(foodInTile == -1) {
                        System.out.println(creatures.get(i).name + " tried to eat, but there was no food nearby");
                        creatures.get(i).idle(1);
                    } else {
                        System.out.println(creatures.get(i).name + " ate " + food.get(foodInTile).name);
                        creatures.get(i).eat(food.get(foodInTile));
                        food.remove(foodInTile);
                    }

                    break;
                case 2 : //Reproduce
                    Creature offspring = creatures.get(i).reproduce();
                    System.out.println(creatures.get(i).name + " reproduced and created " + offspring.name);
                    creatures.add(offspring);
                    newCreatures++;
                    break;
            }

            if(creatures.get(i).dead()) {
                System.out.println(creatures.get(i).name + " died");
                creatures.remove(i);
                deadCreatures++;
            }
        }
    }

    public void spawnCreature() {
        String name = "Creature" + random.nextInt(0, 9999);
        int[] bounds = getBounds();
        int x = random.nextInt(bounds[0], bounds[1] + 1);
        int y = random.nextInt(bounds[2], bounds[3] + 1);

        creatures.add(new Creature(name, x, y));
        newCreatures++;
        System.out.println("Spawned new creature '" + name + "' at position " + x + " " + y);
    }

    public void spawnFood() {
        int value = random.nextInt(1, 10);
        int life = random.nextInt(1, 6);
        String name = "Baked Potato";

        for(int i = 0; i < Math.abs(value - 5); i++) {
            if(value - 5 < 0) {
                name += "-";
            } else {
                name += "+";
            }
        }

        //Spawn food within a certain distance of the furthest creatures
        int[] bounds = getBounds();
        int x = random.nextInt(bounds[0] - 5, bounds[1] + 6);
        int y = random.nextInt(bounds[2] - 5, bounds[3] + 6);
        food.add(new Food(name, value, life, x, y));
        newFood++;
        System.out.println("Spawn new '" + name + "' at position " + x + " " + y);
    }

    private int[] getBounds() {
        int minX = 0, maxX = 0;
        int minY = 0, maxY = 0;
        
        for(int i = 0; i < creatures.size(); i++) {
            int[] pos = creatures.get(i).getPosition();
            minX = Math.min(minX, pos[0]);
            maxX = Math.max(maxX, pos[0]);
            minY = Math.min(minY, pos[1]);
            maxY = Math.max(maxY, pos[1]);
        }

        return new int[] {minX, maxX, minY, maxY};
    }
}