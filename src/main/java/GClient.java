import com.ucha.grpc.User;
import com.ucha.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class GClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",4004).usePlaintext().build();

       //stubs generate from proto
        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word.. 'register' to do Registration OR 'login' to do Login" );
        String flag = scanner.next();

        if(flag.equals("register")){
            //Register
            System.out.print("Registration Id: ");
            long reg = scanner.nextLong();
            System.out.print("Enter Student name: ");
            String name = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();
            User.RegRequest regRequest = User.RegRequest.newBuilder().setRegID(reg).setUsername(name).setPassword(password).build();
            User.APIResponse response = userStub.register(regRequest);
            System.out.println(response.getResponseMessage());
        }
        else if (flag.equals("login")){
            //Login
            System.out.print("Username: ");
            String username = scanner.next();
            System.out.print("Password: ");
            String passcode = scanner.next();
            User.LoginRequest loginRequest = User.LoginRequest.newBuilder().setUsername(username).setPassword(passcode).build();
            User.APIResponse response1 = userStub.login(loginRequest);
            System.out.println(response1.getResponseMessage());
        }
        else {
            System.out.println("register or login first,,,");
        }
    }
}
