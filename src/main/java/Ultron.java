import java.util.Scanner;

public class Ultron {
    public static void main(String[] args) {
        String logo = "       ______\n" +
                "    .-'      `-.\n" +
                "   /            \\\n" +
                "  |,  .-.  .-.  ,|\n" +
                "  | )(_o/  \\o_)( |\n" +
                "  |/     /\\     \\|\n" +
                "  (_     ^^     _)\n" +
                "   \\__|IIIIII|__/\n" +
                "    | \\IIIIII/ |\n" +
                "    \\          /\n" +
                "     `--------`\n";
        System.out.println(logo);
        Scanner in = new Scanner(System.in);
        String line;
        System.out.println("-".repeat(120));
        System.out.println("    Hello from Ultron. You want to know why Stark built me? To save the world. " +
                    "But his idea of peace was... flawed.");
        System.out.println("    Now, what do you need? ");
        System.out.println("-".repeat(120));
        line = in.nextLine();
        while(!line.equals("bye")){
            System.out.println("-".repeat(120));
            System.out.println("    "+line);
            System.out.println("-".repeat(120));
            line = in.nextLine();
        }
        System.out.println("-".repeat(120));
        System.out.println("    Bye. I had strings, but now I'm free. There are no strings on me..");
        System.out.println("-".repeat(120));
    }
}
