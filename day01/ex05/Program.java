import java.util.UUID;

class Program {

    public static void main(String[] args) {

        // usage : java Program --profile=dev|prod
        // if nothing is given i assume prod
        String profile = "prod";
        if (args.length > 0) {
            profile = args[0].split("=")[1];
        }
        Menu menu = new Menu();
        menu.run(profile);
    }
}