import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RemoteDataAdapter implements DataAccess {

    Gson gson = new Gson();

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void saveNote(NoteModel note) {

    }

    @Override
    public NoteModel loadNote(int id) {

        return null;
    }

    @Override
    public NoteListModel searchNote(String keyword) {
        Socket s = null;
        NoteListModel list = null;
        try {
            s = new Socket("localhost", 5056);
            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.FIND_REQUEST;
            req.body = keyword;

            String json = gson.toJson(req);
            dos.writeUTF(json);

            String received = dis.readUTF();

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            list = gson.fromJson(res.body, NoteListModel.class);

            System.out.println("Receiving a NoteListModel object of size = " + list.list.size());


            dis.close();
            dos.close();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}
