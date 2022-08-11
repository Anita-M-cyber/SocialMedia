package controller;

import model.User;
import repo.Repository;
import repo.UserRepo;
import util.Role;

import java.util.Objects;
import java.util.Scanner;

public class LoginController {
    private final char[] UPPER_CASE = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private final int[] NUMERICS = {1,2,3,4,5,6,7,8,9,0};
    private final UserRepo userRepo = new UserRepo();

    public void login(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("enter your username: ");
        String username = scanner.next();
        System.out.print("enter your password(1) , forgot password(2): ");
        int choice = scanner.nextInt();
        String pass;
        if (choice == 1) {
            System.out.print("enter your password: ");
            pass = scanner.next();
            User current = userRepo.auth(username,pass);
            if(!Objects.isNull(current)) {
                Repository.currentUser = current;
                generalController.generalElections();
            }
            else{
                throw new RuntimeException("username or password is wrong!");
            }
        }else if(choice == 2){
            Logger.info("answer security question:");
            System.out.print("what was your first school name?");
            User user = userRepo.checkSecAnswer(username, scanner.next());
            if (Objects.isNull(user)) {
                throw new RuntimeException("No user found with this data!!!!");
            }
            pass = getPass();
            userRepo.save(user.getId(),user.getUsername(),pass,user.getSecurityAnswer(),user.getRole());
            Repository.currentUser = userRepo.get(user.getId());
            generalController.generalElections();
        }
    }

    public void signup(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("enter your username: ");
        String username = scanner.next();
        String password = getPass();
        System.out.println("security Question: \t what was your first school name?");
        String secAns = scanner.next();
        System.out.print("is this a normal account(1) or a business one(2)?");
        int choice = scanner.nextInt();
        Role role;
        if (choice == 1)
            role = Role.Normal;
        else if (choice == 2)
            role = Role.Business;
        else throw new IllegalArgumentException("wrong input!!");

        User user = userRepo.save(0l , username , password , secAns , role);
        if (!Objects.isNull(user))
            Logger.info("account created successfully!!");
        else Logger.error(LoginController.class.getName(),"sth happened during save");
    }

    private Boolean checkPass(String message){
        if (message.length() < 8)
            return false;
        String upper = message.toUpperCase();
        boolean firstTest = false;
        for (char c : UPPER_CASE)
            if (upper.contains(Character.toString(c))) {
                firstTest = true;
                break;
            }
        boolean secondTest = false;
        for (int i : NUMERICS)
            if (upper.contains(Integer.toString(i))) {
                secondTest = true;
                break;
            }
        return firstTest && secondTest;
    }

    public String getPass(){
        String password;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            Logger.info("password should be at least " +
                    "8 characters and should contain numbers and characters");
            System.out.print("enter your password: ");
            password = scanner.next();
            System.out.print("reEnter your password: ");
            String repass = scanner.next();
            if (checkPass(password) && repass.equals(password))
                break;
            else Logger.error("pass and repass are not equal or pass didn't meet the needs!");
        }
        return password;
    }
}
