public class ServerStart{

  public static void main(String[] args){
    PixionaryServer server = new PixionaryServer(1337);
    server.start();
  }

}
