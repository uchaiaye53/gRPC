import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(4004).addService(new UserService()).build();
        server.start();
        System.out.println("server started at port no "+ server.getPort());
        server.awaitTermination();
    }
}
