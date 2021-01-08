package ie.gmit.sw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.filter.Filler;
import ie.gmit.sw.filter.ProcessImage;

public class Runner {
  static Scanner input = new Scanner(System.in);
  /**
   * opens an image file and save stats.
   *
   * @param args yar
   */
  public static void main(String[] args) {
    String option = "6";
    BlockingQueue<FileWithData> listOfFiles = null;
    BlockingQueue<FileWithData> qfileWithDatas = new ArrayBlockingQueue<FileWithData>(100);
    String folderName = "";
    do {
      System.out.println(
          "Please enter what you would like to do \n"
              + "1.Enter Image Directory\n"
              + "2.Select a certain image\n"
              + "3.add a custom filter\n"
              + "4.Exit(but you wouldn't do that, right?)\n");
      option = input.nextLine();
      if (option.equalsIgnoreCase("1")) {

        DiretoryReader readFilesInFolder = new DiretoryReader(qfileWithDatas);
        System.out.println("Please type the file name");
        folderName = input.nextLine();
        final File folder = new File(folderName);
        listOfFiles = readFilesInFolder.listfilesForFolder(folder);

      } else if (option.equalsIgnoreCase("2")) {
        System.out.println("Please type the png name");

        List<Thread> threads = startAllThreads(listOfFiles, folderName);

        waitForAllThreads(threads);

      } else if (option.equalsIgnoreCase("3")) {
        String customFilter = input.nextLine();
        System.out.println(customFilter);
      } else if (option.equalsIgnoreCase("4")) {
        System.out.println("Filtering all images");

        List<Thread> threads = startAllThreads(listOfFiles, folderName);

        waitForAllThreads(threads);

      } else if (option.equalsIgnoreCase("5")) {
        System.out.println("Have a nice day :{");
      } else {
        System.out.println(
            "Error 404, Not a possible option and I my existance of having feelings");
      }

    } while (!option.equalsIgnoreCase("4"));
  }

  private static void waitForAllThreads(List<Thread> threads) {
    for (Thread thread : threads) {
      try {
        thread.join();

      } catch (InterruptedException e) { // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private static List<Thread> startAllThreads(
      BlockingQueue<FileWithData> listOfFiles, String folderName) {
    ThreadForImageProcessing imageProcess1 =
        new ThreadForImageProcessing(listOfFiles, folderName, new ProcessImage());
    ThreadForImageProcessing imageProcess2 =
        new ThreadForImageProcessing(listOfFiles, folderName, new Filler());
    List<Thread> threads = new ArrayList<>();
    threads.add(new Thread(imageProcess1));
    threads.add(new Thread(imageProcess2));
    for (Thread thread : threads) {
      thread.start();
    }
    return threads;
  }
}
