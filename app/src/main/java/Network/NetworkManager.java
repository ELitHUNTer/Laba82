package Network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class NetworkManager {

    private Socket client; //сокет для общения
    private BufferedReader input; // поток чтения из сокета
    private BufferedWriter output; // поток записи в сокет
    private int PORT = 2222;
    private String logged = null;
    private static NetworkManager networkManager = null;
    private Gson gson;
    private boolean release = false;
    public static Activity context;

    private NetworkManager(){
        gson = new Gson();
        connect();
    }

    public String getLogin(){
        if (logged == null)
            return "test";
        return logged.split(" ")[2];
    }

    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 10.0.2.2 - emulator's localhost 192.168.0.105
                    client = new Socket("172.28.16.227", 2222);
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    Log.e("132", "OK");
                } catch (IOException e) {
                    //Log.e("132", "PISDA");
                    e.printStackTrace();
                }
                Log.e("132", "123");
            }
        }).start();

    }

    public static NetworkManager getInstance(){
        if (networkManager == null)
            networkManager = new NetworkManager();
        return networkManager;
    }

    public String executeCommand(String sendingCommand){
        Log.e("Command", sendingCommand);
        FutureTask<String> future = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try
                {
                    Toast t = makeToast("executing");
                    String com = sendingCommand + (logged == null || logged.equals("") ? "" : logged);
                    String[] splittedCommand = com.split(" ");
                    if (CommandType.valueOf(splittedCommand[0]) == CommandType.login || CommandType.valueOf(splittedCommand[0]) == CommandType.register) {
                        splittedCommand[2] = MD5(splittedCommand[2]);
                    }
                    Command command = new Command(CommandType.valueOf(splittedCommand[0]), Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length));
                    if (release && (command.getType() != CommandType.login &&
                            command.getType() != CommandType.register &&
                            command.getType() != CommandType.exit) && logged == null) {
                        makeToast("error");
                        return "Для работы с коллекцией вы должны авторизоваться";
                    }
                    if (command.getType() == CommandType.exit) {
                        client.close();
                        input.close();
                        output.close();
                        System.exit(0);
                    }
                    if (command.getType() == CommandType.login && logged == null) {
                        logged = "";
                    }
                    if (command.getType() == CommandType.login && logged != null && !logged.equals("")) {
                        makeToast("error");
                        return "Перед тем, как войти в другой аккаунт необходимо выйти из текущего";
                    }
                    if (command.getType() == CommandType.register) {

                    }
                    send(gson.toJson(command));
                    if (logged != null && logged.equals("")) {
                        String[] s = gson.fromJson(input.readLine(), Message.class).getMessage();
                        if (Boolean.valueOf(s[0])) {
                            logged = " -l " + splittedCommand[1] + " " + splittedCommand[2];
                            return "Вход выполнен";
                        } else {
                            logged = null;
                            return "Неправильный логин/пароль";
                        }
                    } else {
                        String s = "";
                        makeToast("ok");
                        for (String a : gson.fromJson(input.readLine(), Message.class).getMessage())
                            s += a + "\n";
                        return s;
                    }
                } catch(
                        IllegalArgumentException ex)

                {
                    String s = "Нет такого типа команды. Команда должна быть одним из следующих типов:\n";
                    for (CommandType ct : CommandType.values())
                        s += ct + "\n";
                    return s;
                } catch(
                        SocketException ex)

                {
                    makeToast("Server error");
                    return "Сервер временно недоступен";
                } catch(
                        IOException e)

                {
                    return e.getMessage();
                }
            };
        });
        new Thread(future).start();
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return e.getMessage();
        }
    }

    public void signOut(){
        logged = null;
    }

    private void send(String message){
        try {
            output.write(message + '\n');
            output.flush();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private Toast makeToast(String s){
        final Toast[] t = new Toast[1];
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t[0] = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                t[0].show();
            }
        });
        return t[0];
    }

    private String MD5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}