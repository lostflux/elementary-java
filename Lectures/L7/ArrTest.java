public class ArrTest {
    public static void main(String[] args) throws Exception {
//        int[] numbers = new int[10];
//        numbers[2] = 23;
//        numbers[5] = 1;
//        Random random = new Random();
//        for (int i=0; i<10; i++) {
//            numbers[i] = random.nextInt();
//        }
//
//        for (int number : numbers) {
//            System.out.println(number);
//        }
//        System.out.println(Arrays.toString(numbers));
//
//        ArrayList<Integer> numbers2 = new ArrayList<>();
//        for (int i=0; i<10; i++) {
//            numbers2.add(random.nextInt());
//        }
//
//        for (int number : numbers2) {
//            System.out.println(number);
//        }
//        System.out.println(numbers2);

        GrowingList<Integer> arr = new GrowingList<>();
        arr.append(3);
        arr.append(25);
        arr.append(5);
        arr.append(3);
        arr.append(25);
        arr.append(5);
        arr.append(3);
        arr.append(25);
        arr.append(5);
        arr.append(3);
        arr.append(25);
        arr.append(5);
        arr.append(3);
        arr.append(25);
        arr.append(5);

        System.out.println(arr.size());
        arr.append(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
//        arr.append(1, 2, 3, 4,5);
        System.out.println(arr);
        System.out.println(arr.size());

//        arr.append([21, 22, 23]);
        GrowingList<Integer> temp2 = new GrowingList<>();
        temp2.append(101, 102, 103, 104);
        System.out.println(temp2);
        arr.append(temp2);
        String me = "Amittai";
        String what = "Who is" + me;
        System.out.println("what" +  "that");
        System.out.println(arr);
        arr.concat(temp2);
        System.out.println(arr);
        arr.sort();
        System.out.println(arr);


    }
}