import java.util.Scanner;

class Program {

    // returns the index of a string in an array
    public static int findIndex(String[] array, String s) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                break;
            }
            if (array[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }


    public static String trim(String str) {
        char[] chars = str.toCharArray();
        String result = "";
        for (char c : chars) {
            if (c != ' ') {
                result += c;
            }
        }
        return result;
    }

    // Parse an integer from a string
    public static int parseInt(String s) {
        int result = 0;
        int sign = 1;
        int i = 0;

        char[] chars = s.toCharArray();
        // if nagative
        if (chars[0] == '-') {
            sign = -1;
            i++;
        }

        for (char c : chars) {
            if (c >= '0' && c <= '9') {
                result = result * 10 + (c - '0');
            } else {
                return -1;
            }
        }
        return result * sign;
    }

    // findIndex

    // split the string by the separator
    public static String[] split(String s, char sep) {
        String[] tmp_res = new String[5];
        int index = 0;
        char[] tmp = s.toCharArray();
        String tmpStr = "";

        for (char c : tmp) {
            if (c == sep) {
                tmp_res[index] = tmpStr;
                tmpStr = "";
                index++;
            } else {
                tmpStr += c;
            }
        }
        tmp_res[index] = tmpStr;
        String[] result = new String[index + 1];
        for (int i = 0; i < index + 1; i++) {
            result[i] = trim(tmp_res[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] names = new String[10];
        // student's tracker
        int n = 0;
        while (true) {
            String input = sc.nextLine();
            if (input.equals(".")) {
                break;
            } else {
                names[n] = trim(input);
                n++;
            }
        }

        // Populating a timetable
        // String[] times = { "1", "2", "3", "4", "5", "6" };
        String[] days = { "TU", "WE", "TH", "FR", "SA", "SU", "MO" };
        int[] times = {1, 2,3,4,5,6};
        /*
         * storing the schedule of up to 10 classes, each row contains the time and day
         * indices
         */

        int[][] classes = new int[50][2];
        // class tracker
        int m = 0;
        while (true) {
            String input = sc.nextLine();
            if (input.equals(".")) {
                break;
            } else { // assuming that format of input is "time day"
                String[] parts = split(input, ' ');
                int t = parseInt(parts[0]);
                String d = parts[1]; 
                classes[m][0] = findIndex(days, d); // storing the day index
                classes[m][1] = t - 1; // storing the time index
                m++;
            }
        }

        // populating the shedule
        // Calculate the number of weeks in September 2020
        int weeksInMonth = 5;
        int daysInMonth = 30;

        // Create a schedule array to store the occurrences of each class in the month
        // [6] for day, [0-5] for hours
        int[][][] schedule = new int[weeksInMonth][7][7];
        // Iterate over the classes array and fill in the schedule array with the
        // occurrences of each class


        // classes
        for (int i = 0; i < m; i++) {
            int dayIndex = classes[i][0];
            int timeIndex = classes[i][1];
            for (int j = 0; j < weeksInMonth; j++) {
                int dayInMonth = j * 7 + dayIndex + 1;
                if (dayInMonth <= daysInMonth) {
                    schedule[j][dayIndex][6] = dayInMonth;
                    schedule[j][dayIndex][timeIndex] = times[timeIndex];
                }
            }
        }

        // populating the attendance

        int[][][] attendanceStatuses = new int[n][30][6];

        // first filling all indexes where schedule is not zero
        for (int i = 0; i < weeksInMonth; i++) {
            for (int j = 0; j < 7; j++) {
                for (int z = 0; z < 6; z++) {

                if (schedule[i][j][z] != 0) {
                    int dayIndex = (schedule[i][j][6]) - 1;
                    // System.out.println("day index is " + dayIndex);
                    // int timeIndex = schedule[i][j][1];
                    // int dayInMonth = schedule[i][j][0];
                    for (int k = 0; k < n; k++) {
                        attendanceStatuses[k][dayIndex][z] = -2;
                    }
                }
                }
            }
        }

        int index = 0;
        while (true) {
            String input = sc.nextLine();
            if (input.equals(".")) {
                break;
            } else {
                String[] parts = split(input, ' ');
                int studentIndex = findIndex(names, parts[0]);
                int timeIndex = parseInt(parts[1]) - 1;
                int dayIndex = (parseInt(parts[2]) - 1) % 7;
                int dayInMonth = parseInt(parts[2]);
                for (int i = 0; i < weeksInMonth; i++) {
                    // if (schedule[i][dayIndex][6] == dayInMonth && schedule[i][dayIndex][1] == timeIndex) {
                        if (schedule[i][dayIndex][6] == dayInMonth) {
                            for (int z = 0; z < 6; z++) {
                                if (schedule[i][dayIndex][z] == times[timeIndex]) {
                                    attendanceStatuses[studentIndex][dayInMonth - 1][z] = parts[3].equals("HERE") ? 1 : -1;
                                }
                            }
                        }
                }
            }
        }

        // for (int i =0; i < t)

        // printing stuff in the above format
        int firstTime = 0;
        for (int i = 0; i < weeksInMonth; i++) {
            for (int j = 0; j < 7; j++) {
                // 4:00 WE 2|
                if (schedule[i][j][6] != 0) {
                    int dayIndex = (schedule[i][j][6] - 1) % 7;
                    // int timeIndex = schedule[i][j][1];
                    if (firstTime == 0) {
                        System.out.print("   ");
                    }
                    firstTime++;
                    // if first time we're printing ever, print a space before
                    for (int z = 0; z < 6; z++) {
                        if (schedule[i][j][z] != 0) {
                            System.out.print(times[z] + ":00 " + days[dayIndex] + " " + schedule[i][j][6] + "|");
                        }
                    }
                }
            }
        }
        int[] arr = new int[2];
        int hey = -1;
        System.out.println();
        // printing names and their attendance
        for (int i = 0; i < n; i++) {
            hey += 1;
            System.out.print(names[i] + " ");
            for (int j = 0; j < 30; j++) {
                for (int z = 0; z < 6; z++)  {
                if (attendanceStatuses[i][j][z] != 0) {
                    if (j > 9) {
                        System.out.print("         ");
                    } else {
                        System.out.print("      ");
                    }
                    // System.out.print(" ");
                    if (attendanceStatuses[i][j][z] == 1) {
                        System.out.print(" 1|");
                    } else if (attendanceStatuses[i][j][z] == -1) {
                        System.out.print("-1|");
                    } else {
                        System.out.print("  |");
                    }
                }
                }
            }
            System.out.println("");
        }
    }
}
