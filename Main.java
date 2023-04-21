import java.util.Scanner;

class Main {
    public static void main(final String[] args) {
        World world = new World(5, 3);
        Scanner scanner = new Scanner(System.in);
        String input;
        
        System.out.println("Type 'q' to quit, 'd' to display creatures, or enter to continue.");
        System.out.print("Option: ");
        input = scanner.nextLine();
        
        while(!input.equals("q")) {

            if(!input.equals("d")) {
                world.step();
                world.display();
            } else {
                world.displayCreatures();
            }

            System.out.print("Option: ");
            input = scanner.nextLine();
        }

        scanner.close();
    }
}