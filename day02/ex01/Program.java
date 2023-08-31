
package day02.ex01;

class Program {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 2) {
            System.err.println("Usage: java Program <file_path1> <file_path2>");
            System.exit(-1);
        }
        CosineSimilariy cs = new CosineSimilariy(args[0], args[1]);
        System.out.printf("Similarity = %.2f\n", cs.calculateCosSim());
    }

}
