import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverSocket {

    private static List<Socket> clients = new ArrayList<>();

    private InputStream input;
    private InputStreamReader inputReader;
    private BufferedReader bufferReader;

    private PrintWriter outputWriter;//用以輸出string到client
    private static Map<Integer,chatRoom> allRoom = new HashMap<>();
    private Map<Integer,List<Socket>> roomMemberSocket = new HashMap<>();
    private static int key = 0;
    private static int roomNum = 0;
    public serverSocket() throws UnknownHostException {
    }

    private void start() {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            // 開啟服務，設置指定端口
            //InetAddress addr = InetAddress.getByName("192.168.0.11");
            ServerSocket server = new ServerSocket(25566);
            System.out.println("服務開啟，等待客戶端連接中...");
            // 循環監聽
            while (true) {
                // 等待客戶端進行連接
                Socket client = server.accept();
                System.out.println("客戶端[" + client.getRemoteSocketAddress() + "]連接成功，當前房間總數" + roomNum + "個");
                //先送出所有room給client用以建立組隊大廳，取得並判斷此client連線執行功能， 1 建立新room 2 加入已存在room
                Thread send = new sendAllRoom(client);
                send.start();
                // 將客戶端添加到集合
                clients.add(client);
                // 每一個客戶端開啟一個線程處理消息
                Thread cAct = new clientAct(client);
                cAct.start();
                //new MessageListener(client).start();
            }
        } catch (IOException e) {
            // log
        }
        executor.shutdown();

    }
    class sendAllRoom extends Thread {
        private Socket client;
        public sendAllRoom(Socket client){this.client = client; }
        @Override
        public void run(){
            try {
                PrintWriter pw = new PrintWriter(client.getOutputStream());
                ArrayList<chatRoom> chRoom = new ArrayList(allRoom.values());
                System.out.println(chRoom.size());
                Gson gson = new Gson();
                pw.println(gson.toJson(chRoom));
                pw.flush();
                System.out.println("do get all room");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.printf("sendall\n");
        }
    }
    public class clientAct extends Thread{
        private Socket newClient;

        private InputStream clientInput;
        private InputStreamReader clientInputReader;
        private BufferedReader clientBufferReader;

        private PrintWriter clientWriter;
        //public static ExecutorService listenerExecutor = Executors.newCachedThreadPool();
        private Thread sMSG;
        public clientAct(Socket client){this.newClient = client;}
        @Override
        public void run(){
            while(true)
            {
                System.out.println("control");
                String control = "";
                try {
                    clientWriter = new PrintWriter(newClient.getOutputStream());
                    clientInput = newClient.getInputStream();
                    clientInputReader = new InputStreamReader(clientInput);
                    clientBufferReader = new BufferedReader(clientInputReader);
                    control = clientBufferReader.readLine();
                } catch (SocketException e) {
                    System.out.println("客戶端" + newClient.getRemoteSocketAddress() + "嗝屁了");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                switch (control){
                    case "getAllRoom":
                        PrintWriter pw = null;
                        try {
                            pw = new PrintWriter(newClient.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ArrayList<chatRoom> chRoom = new ArrayList(allRoom.values());
                        System.out.println(chRoom.size());
                        Gson gson = new Gson();
                        pw.println(gson.toJson(chRoom));
                        pw.flush();
                        break;
                    case "create":
                        try {
                            roomNum++;
                            createRoom();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "join":
                        try {
                            joinRoom();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("error!!No function.");
                }
            }
        }
        public void createRoom() throws Exception {//建立一新組隊房間
            String jsonObject = clientBufferReader.readLine();
            chatRoom room = JSON.parseObject(jsonObject,chatRoom.class);
            chatRoom newRoom = new chatRoom(room,key);

            List<Socket> newMS = new ArrayList<>();
            roomMemberSocket.put(key,newMS);

            allRoom.put(key++,newRoom);//房間建立成功
            System.out.println("所有房間: " + newRoom);
            String s = JSON.toJSONString(newRoom);
            //String sa = JSON.toJSONString(new chatRoom());
            //JSON.toJSONString(newRoom)
            clientWriter.println(s);
            clientWriter.flush();

            sMSG = new MessageListener(newClient,newRoom.getPlayer().get(0), key-1);
            sMSG.start();
            sMSG.join();

        }
        public void joinRoom()throws Exception {
            int roomKey = Integer.parseInt(clientBufferReader.readLine());
            String us = clientBufferReader.readLine();
            Player user = JSON.parseObject(us,Player.class);
            allRoom.get(roomKey).addMember(user);
            chatRoom c = new chatRoom(allRoom.get(roomKey));
            //allRoom.get(roomKey)
            clientWriter.println(JSON.toJSONString(c));//寄回完整防資訊，包刮key 跟成員
            clientWriter.flush();

            sMSG = new MessageListener(newClient, user, roomKey);
            sMSG.start();
            sMSG.join();
        }
    }
    /**
     * 消息處理線程，負責轉发消息到聊天室里的人
     */
    class MessageListener extends Thread {

        // 將每個連接上的客戶端傳遞進來，收消息和发消息
        private Socket client;
        private int key;
        private chatRoom room;
        private Player user;
        // 將這幾個變量抽出來公用，避免頻繁new對象
        private OutputStream os;
        private PrintWriter pw;
        private InputStream is;
        private InputStreamReader isr;
        private BufferedReader br;

        public MessageListener(Socket socket,Player user,int key) {
            this.client = socket;
            this.key = key;
            this.user = user;
            room = allRoom.get(key);
        }

        @Override
        public void run() {
            try {
                // 每個用戶連接上了，就发送一條系統消息（類似於廣播）
                pw = new PrintWriter(client.getOutputStream());
                //將client socket 加入房間
                List<Socket> newMS = new ArrayList<>(roomMemberSocket.get(key));
                newMS.add(client);
                roomMemberSocket.put(key,newMS);
                sendMsg(0, "[系統消息]：" + user.getName() + "進入房間!! ");
                System.out.println("房間成員: "+newMS);

                // 循環監聽消息
                while (true) {
                    int out = sendMsg(1, "[" + user.getName() + "]：" + receiveMsg());
                    if(out == 0)
                    {
                        System.out.println("out");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
                allRoom.get(key).signOut(user);
                pw.println(JSON.toJSONString(user));
            }
        }

        private int sendMsg(int type, String msg) throws IOException {
            if (type != 0) {
                System.out.println("處理消息：" + msg);
            }
            String comp = msg;
            System.out.println(comp);
            if(comp.equals("[" + user.getName() + "]：xu6d9z;6ru0"))
            {
                System.out.println("離開");
                List<Socket> delMS = new ArrayList<>(roomMemberSocket.get(key));
                delMS.remove(client);
                roomMemberSocket.put(key,delMS);
                allRoom.get(key).signOut(user);
                msg = user.getName() + "已離開房間。";
                for (Socket socket : roomMemberSocket.get(key)) {
                    os = socket.getOutputStream();
                    pw = new PrintWriter(os);
                    pw.println(msg);// 這里需要特別注意，對方用readLine獲取消息，就必須用print而不能用write，否則會導致消息獲取不了
                    pw.flush();
                }
                if(roomMemberSocket.get(key).size() == 0){
                    roomMemberSocket.remove(key);
                    allRoom.remove(key);
                    System.out.println("摧毀"+key+"房間");
                    return 0;
                }
                return 0;
            }
            if(roomMemberSocket.get(key) != null)
            {
                for (Socket socket : roomMemberSocket.get(key)) {
                    os = socket.getOutputStream();
                    pw = new PrintWriter(os);
                    pw.println(msg);// 這里需要特別注意，對方用readLine獲取消息，就必須用print而不能用write，否則會導致消息獲取不了
                    pw.flush();
                }
            }
            return 1;
        }
        /**
         * 接收消息
         *
         * @return 消息內容
         * @throws IOException
         */
        private String receiveMsg() throws IOException {
            is = client.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            return br.readLine();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        new serverSocket().start();
    }

}