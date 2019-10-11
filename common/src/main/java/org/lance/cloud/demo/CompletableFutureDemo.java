package org.lance.cloud.demo;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture示例
 *
 * @author Lance
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        CompletableFutureDemo demo = new CompletableFutureDemo();

        demo.allOf();
    }

    /**
     * supplyAsync支持返回值
     *
     * @throws Exception
     */
    public void supplyAsync() throws Exception {
        Long time = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return System.currentTimeMillis();
        }).get(5, TimeUnit.SECONDS);

        System.out.println("Result " + time);
    }

    /**
     * runAsync不支持返回值
     *
     */
    public void runAsync() {
        CompletableFuture.runAsync(() -> sleep(1))
                // 执行当前任务的线程执行继续执行 whenComplete 的任务
                .whenComplete((value, throwable) -> System.out.println("Run success"))
                // 执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行
                .whenCompleteAsync((value, throwable) -> System.out.println("Run success"))
                // 返回一个新的CompletableFuture，当CompletableFuture完成时完成，结果是异常触发此CompletableFuture的完成特殊功能的给定功能;
                // 否则，如果此CompletableFuture正常完成，则返回的CompletableFuture也会以相同的值正常完成。
                .exceptionally(throwable -> {
                    System.out.println("Run fail");
                    return null;
                })
                // join()不能中断，但不会抛出检查异常
                .join();
    }

    /**
     * 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化
     *
     * handle 是在任务完成后再执行，还可以处理异常的任务。
     * thenApply 只可以执行正常的任务，任务出现异常则不执行 thenApply 方法。
     *
     * @throws Exception
     */
    public void thenApplyAndHandle() throws Exception {
        CompletableFuture.supplyAsync(System::currentTimeMillis)
                // 第二个任务依赖第一个任务的结果
                .thenApply(prev -> prev / 1000)
                //  handle 是在任务完成后再执行，还可以处理异常的任务
                .handle((prev, throwable) -> {
                    if (throwable != null) {
                        System.out.println(throwable.getMessage());
                        return -1L;
                    }
                    return prev / 1000;
                })
                .get();
    }

    /**
     * thenAccept 消费处理结果
     * thenRun 不关心任务的处理结果
     *
     * @throws Exception
     */
    public void thenAcceptAndRun() throws Exception {
        CompletableFuture.supplyAsync(System::currentTimeMillis)
                // 接收任务的处理结果，并消费处理，无返回结果
                .thenAccept(result -> System.out.println(result / 1000))
                // 不关心任务的处理结果
                .thenRun(() -> System.out.println("Just run"))
                .get();
    }


    /**
     * thenCombine 会把两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块处理，有返回值
     * thenAcceptBoth 会把两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块处理，无返回值
     *
     * @throws Exception
     */
    public void thenCombineAndAcceptBoth() throws Exception{
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(System::currentTimeMillis);

        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(() -> new Random().nextLong());

        Long result = future1.thenCombine(future2, (one, two) -> one + two).get();
        System.out.println(result);

        future1.thenAcceptBoth(future2, (one, two) -> System.out.println(one + two));
    }

    /**
     * applyToEither 两个CompletionStage，谁执行返回的结果快，就用那个CompletionStage的结果进行下一步的转化操作
     * acceptEither 两个CompletionStage，谁执行返回的结果快，就用那个CompletionStage的结果进行下一步的消耗操作
     */
    public void applyToEitherAndAcceptEither() throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            int rand = new Random().nextInt(5);
            sleep(rand);
            return "f1 - " + rand;
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            int rand = new Random().nextInt(5);
            sleep(rand);
            return "f2 - " + rand;
        });

        String message = future1.applyToEither(future2, (result) -> "Fast: " + result).get();
        System.out.println(message);

        future1.acceptEither(future2, (result) -> System.out.println("Fast: " + result));
    }

    /**
     * runAfterEither 两个CompletionStage，任何一个完成了都会执行下一步的操作
     * runAfterBoth 两个CompletionStage，都完成了计算才会执行下一步的操作
     *
     */
    public void runAfterEitherAndRunAfterBoth() throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            int rand = new Random().nextInt(5);
            sleep(rand);
            return "f1 - " + rand;
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            int rand = new Random().nextInt(5);
            sleep(rand);
            return "f2 - " + rand;
        });

        future1.runAfterEither(future2, () -> System.out.println("One of them has been complete."));

        future1.runAfterBoth(future2, () -> System.out.println("Both complete")).get();
    }

    /**
     * 多个CompletionStage都完成了执行下一步的操作
     */
    public void allOf() {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> System.out.println("future1 complete"));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> System.out.println("future2 complete"));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> System.out.println("future3 complete"));
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> {
            sleep(2);
            System.out.println("future4 complete");
        });

        CompletableFuture.allOf(future1, future2, future3).runAfterBoth(future4, () -> System.out.println("Both complete")).join();
    }


    /**
     * thenCompose 方法允许对两个 CompletionStage 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作
     * @throws Exception
     */
    public void thenCompose() throws Exception {
        String message = CompletableFuture
                .supplyAsync(() -> {
                    int rand = new Random().nextInt(5);
                    return "f1 - " + rand;
                })
                .thenCompose(result -> CompletableFuture.supplyAsync(() -> result + " thenCompose"))
                .get();

        System.out.println(message);
    }

    /**
     *
     *
     * @param time
     */
    private void sleep(int time) {
        try {
            System.out.println("Running " + time + " second...");
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
