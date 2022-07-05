import com.ucha.grpc.User;
import com.ucha.grpc.userGrpc;
import io.grpc.stub.StreamObserver;

import java.sql.*;


public class UserService extends userGrpc.userImplBase{

    public static String url = "jdbc:mysql://localhost:3306/student_register";
    public static String user = "root";
    public static String passcode = "";

    // register
    @Override
    public void register(User.RegRequest request, StreamObserver<User.APIResponse> responseObserver){
        System.out.println("inside register...");
        long regId = request.getRegID();
        String userName = request.getUsername();
        String password = request.getPassword();
        User.APIResponse.Builder response = User.APIResponse.newBuilder();

        try {
            Connection connection = DriverManager.getConnection(url, user, passcode);

            String query = "insert into register values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,regId);
            preparedStatement.setNString(2,userName);
            preparedStatement.setNString(3,password);

            String query2 = "select * from register where RegNo =?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setLong(1,regId);

            ResultSet resultSet = preparedStatement2.executeQuery();
            if (resultSet.next()){
                response.setResponseCode(100).setResponseMessage("User already exist");
            }else {
                preparedStatement.executeUpdate();
                response.setResponseCode(200).setResponseMessage("Registration successful");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    // login
    @Override
    public void login(User.LoginRequest request, StreamObserver<User.APIResponse> responseObserver){
        System.out.println("inside login ...");
        String userName = request.getUsername();
        String password = request.getPassword();
        User.APIResponse.Builder response = User.APIResponse.newBuilder();

        try {
            Connection connection = DriverManager.getConnection(url, user, passcode);

            String query ="select * from register where Name =? && Password =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setNString(1,userName);
            preparedStatement.setNString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String dbName = resultSet.getString("Name");
                String dbPassword = resultSet.getString("Password");
                if(userName.equals(dbName) && password.equals(dbPassword)){
                    response.setResponseCode(300).setResponseMessage("Login successful");
                }else if(userName.equals(dbName) && (!password.equals(dbPassword))){
                    response.setResponseCode(400).setResponseMessage("Invalid password");
                }
            }else {
                response.setResponseCode(404).setResponseMessage("Invalid user");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.Empty request, StreamObserver<User.APIResponse> responseObserver) {
        super.logout(request, responseObserver);
    }
}
