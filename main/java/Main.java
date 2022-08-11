import controller.Logger;
import controller.LoginController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while(true){
            Logger.info(" 1.login \n 2.signup \n 3.exit \n");
            System.out.println("your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            LoginController controller = new LoginController();
            try {
                if (choice == 1)
                    controller.login();
                else if (choice == 2)
                    controller.signup();
                else if (choice == 3)
                    break;
                else Logger.error("wrong input!!");
            }catch (Exception e){
                Logger.error(e.getMessage());
            }
        }
    }
}
