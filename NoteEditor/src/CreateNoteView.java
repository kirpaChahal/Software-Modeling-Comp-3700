import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CreateNoteView {
    private JTextField idTextField;
    private JTextField titleTextField;
    private JTextField bodyTextField;
    private JButton saveButton;
    private JButton loadButton;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;

    public CreateNoteView() {
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idTextField.getText();
                loadAndDisplayNote(id);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NoteModel noteModel = new NoteModel();
                noteModel.id = Integer.parseInt(idTextField.getText());
                noteModel.title = titleTextField.getText();
                noteModel.body = bodyTextField.getText();
                saveNoteToServer(noteModel);
            }
        });
    }

    private void saveNoteToServer(NoteModel noteModel) {
        try
        {

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // -> connect to the server

            Gson gson = new Gson();

            // -> convert the RequestModel to json(text)

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.SAVE_REQUEST; // id of the request
            req.body = gson.toJson(noteModel); //

            String json = gson.toJson(req);
            dos.writeUTF(json); // send the request to the server
            // the program will pause here to wait for the response from the server
            String received = dis.readUTF();  //

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
            }
            else if (req.code == RequestModel.LOAD_REQUEST) {
                NoteModel model = gson.fromJson(res.body, NoteModel.class);
                // update the ui

                titleTextField.setText(model.title);
                bodyTextField.setText(model.body);
            } else if (req.code == RequestModel.FIND_REQUEST) {
                NoteListModel list = gson.fromJson(res.body, NoteListModel.class);
                System.out.println("Receiving a NoteListModel object of size = " + list.list.size());
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadAndDisplayNote(String id) {
        try
        {

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // -> connect to the server

            Gson gson = new Gson();

            // -> convert the RequestModel to json(text)

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.LOAD_REQUEST; // id of the request
            req.body = id; //

            String json = gson.toJson(req);
            dos.writeUTF(json); // send the request to the server
            // the program will pause here to wait for the response from the server
            String received = dis.readUTF();  //

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
            }
            else if (req.code == RequestModel.LOAD_REQUEST) {
                NoteModel model = gson.fromJson(res.body, NoteModel.class);
                // update the ui

                titleTextField.setText(model.title);
                bodyTextField.setText(model.body);
            } else if (req.code == RequestModel.FIND_REQUEST) {
                NoteListModel list = gson.fromJson(res.body, NoteListModel.class);
                System.out.println("Receiving a NoteListModel object of size = " + list.list.size());
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
