import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SongFei on 2019/3/24.
 */
public class clientSocket {

    private Socket server = null;
    private List<chatRoom> allRoom = new ArrayList<chatRoom>();

    private ExecutorService executor = Executors.newCachedThreadPool();
    private OutputStream os;
    private PrintWriter pw;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader br;

    public List<chatRoom> getAllRoom() {
        return allRoom;
    }
    public void start() {
        try {
            // 連接服務器127.0.0.1 //180.177.24.44 // 192.168.0.11 // 10.18.129.99
            server = new Socket("127.0.0.1", 25566);
            System.out.println("連接服務器成功，身份證：" + server.getLocalSocketAddress());

            // 啟動接受消息的線程
            os = server.getOutputStream();
            br = new BufferedReader(new InputStreamReader(server.getInputStream()));
            //獲取所有房間
            Type listType = new TypeToken<List<chatRoom>>() {}.getType();
            Gson gson = new Gson();
            allRoom = gson.fromJson(br.readLine(),listType);
            if(allRoom.isEmpty())
                System.out.println("there is no room to join,Do you want to create one ?");
            else
                System.out.println(allRoom);
        } catch (SocketException e) {
            System.out.println("服務器" + server.getRemoteSocketAddress() + "嗝屁了");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<chatRoom> recvAllRoom(){
        List<chatRoom> allR = new ArrayList<>();
        try {
            sendMsg("getAllRoom");
            Type listType = new TypeToken<List<chatRoom>>() {}.getType();
            Gson gson = new Gson();
            allRoom = gson.fromJson(br.readLine(),listType);
            System.out.println("try get room");
            return allRoom;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allR;
    }
    public chatRoom createRoom(chatRoom basic){
        try {
            sendMsg("create");
            System.out.println("請求創建房間");
            PrintWriter writer = new PrintWriter(server.getOutputStream());

            writer.println(JSON.toJSONString(basic));
            writer.flush();
            System.out.println("傳入必要資訊");

            chatRoom cr = JSON.parseObject(br.readLine(),chatRoom.class);
            //chatRoom room = cr.get(0);
            System.out.println("接收已建立新房");
            //executor.execute(new SendMessageListener());
            //executor.execute(new ReceiveMessageListener());
            return cr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public chatRoom joinRoom(int key,Player user){
        chatRoom  cr = new chatRoom();
        try {
            sendMsg("join");
            System.out.println("請求加入房間");
            PrintWriter writer = new PrintWriter(server.getOutputStream());
            writer.println(key);//傳送key和user，用以決定加入哪個房間
            writer.flush();
            writer.println(JSON.toJSONString(user));
            writer.flush();
            System.out.println("給出必要資訊");
            cr = JSON.parseObject(br.readLine(),chatRoom.class);
            System.out.println("房間資訊"+cr);
            //executor.execute(new SendMessageListener());
            //executor.execute(new ReceiveMessageListener());
            return  cr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cr;
    }
    public void leave(){
        try {
            sendMsg("xu6d9z;6ru0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送消息的線程
     */
    class SendMessageListener extends Thread {
        @Override
        public void run() {
            try {
                // 監聽idea的console輸入
                Scanner scanner = new Scanner(System.in);
                // 循環處理，只要有輸入內容就立即发送
                while (true) {
                    sendMsg(scanner.next());
                }
            } catch (SocketException e) {
                System.out.println("服務器" + server.getRemoteSocketAddress() + "嗝屁了");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接受消息的線程
     */
    class ReceiveMessageListener extends Thread {
        @Override
        public void run() {
            try {
                // 循環監聽，除非掉線，或者服務器宕機了
                while (true) {
                    System.out.println(receiveMsg());

                }
            } catch (SocketException e) {
                System.out.println("服務器" + server.getRemoteSocketAddress() + "嗝屁了");
            } catch (IOException e) {
                // log
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息內容
     * @throws IOException
     */
    public void sendMsg(String msg) throws IOException {
        os = server.getOutputStream();
        pw = new PrintWriter(os);
        pw.println(msg);// 這里需要特別注意，對方用readLine獲取消息，就必須用print而不能用write，否則會導致消息獲取不了
        pw.flush();
    }

    /**
     * 接受消息
     *
     * @return 消息內容
     * @throws IOException
     */
    public String receiveMsg() throws IOException {
        is = server.getInputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        try {
            return br.readLine();
        }
        catch (SocketException e){
            return "error";
        }
    }

    public static void main(String[] args) {
        clientSocket c = new clientSocket();
        Scanner input = new Scanner(System.in);
        c.start();
        if (input.nextInt() == 1)
        {
            chatRoom newRoom = new chatRoom("ccc",123.4,"銀牌");
            newRoom.addMember(new Player("老趙",Rank.BRONZE,123));
            System.out.println(newRoom.getPlayer().get(0));
            chatRoom wa = c.createRoom(newRoom);
            //c.joinRoom(0,new Player("老劉",Rank.BRONZE,123));
        }

    }
}
