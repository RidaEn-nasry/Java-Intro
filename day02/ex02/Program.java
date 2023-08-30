package day02.ex02;

import java.util.Scanner;

class Program {
    public static void main(String args[]) {
        if (args.length < 1) {
            System.err.println("Usage: java Program --current-folder=path/to/folder");
            System.exit(-1);
        }
        String dirPath = args[0].split("=")[1];
        Cmd cmd = null;
        try {
            cmd = new Cmd(dirPath);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        System.out.println(cmd.getDirPath());
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String[] command = input.nextLine().split(" ");
            if (!command[0].equals("ls") && command.length < 2) {
                if (command[0].equals("cd")) {
                    System.err.println("Usage: cd <folder>");
                    continue;
                } else if (command[0].equals("mv") && command.length < 3) {
                    System.err.println("Usage mv <folder | file> <folder | file>");
                    continue;
                }
            }

            switch (command[0]) {
                case "ls":
                    cmd.ls();
                    break;
                case "cd":
                    cmd.cd(command[1]);
                    break;
                case "mv":
                    cmd.mv(command[1], command[2]);
                    break;

                default:
                    System.err.println("err: Invalid Command");
                    break;
            }
        }
        input.close();
    }

}
