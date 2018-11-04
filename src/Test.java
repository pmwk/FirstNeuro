public interface Test {

    void test1();


    default void test2() {
        System.out.println("ewrq");
    }

}
