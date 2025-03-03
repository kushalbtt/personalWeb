class fibIteratives {

    private int count = 0;
    private int count2 = 0;

    public long fib2(int n) {
        count2++;
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib2(n - 1) + fib2(n - 2);
    }

    public int getCount2() {
        return count2;
    }

    public long fib(int n) {

        long i = 0;
        long s = 1;
        long total = 0;
        count = 2;
        for (int k = 2; k <= n; k++) {

            count++;
            total = i + s;
            i = s;
            s = total;

        }
        return s;
    }

    public int getCount() {
        return count;
    }
}
