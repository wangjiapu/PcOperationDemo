package pcMain;

import Utils.SaveInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GUI {
    private JFrame frame;
    private JTextField username;
    private JPasswordField pwd;
    private JButton loginbt;

    private JButton ok_sigin;//注册确定
    private JButton close;
    private JLabel signin;

    private Dialog dialog;
    private Dialog login_Error_dialog;
    private Dialog nameError_dialog;
    private Dialog signinDialog;
    private Dialog noSigninDialog;
    private Dialog signin_error_Dialog;
    private JTextField username_signin;
    private JPasswordField pwd_signin;
    private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = 700;
    private int height = 500;
    private String[] info;

    public GUI(){
        info= SaveInfo.getinfo();
        initGUI();
        initClick();
    }

    private void initGUI() {
        JPanel p1=new JPanel(new GridLayout(1,3,10,1));
        JLabel label=new JLabel("用户名");

        label.setHorizontalAlignment(SwingConstants.RIGHT);
        username=new JTextField(20);
        username.setText(info[0]==null?"":info[0]);
        p1.add(label);
        p1.add(username);
        p1.add(new JPanel());

        JPanel p2=new JPanel(new GridLayout(1,3,10,1));
        JLabel pwdLabal=new JLabel("密码");
        pwdLabal.setHorizontalAlignment(SwingConstants.RIGHT);
        pwd=new JPasswordField(20);
        pwd.setText(info[1]==null?"":info[1]);

        p2.add(pwdLabal);
        p2.add(pwd);
        p2.add(new JPanel());

        JPanel p3=new JPanel(new GridLayout(1,5,10,1));
        loginbt=new JButton("登录");
        loginbt.setHorizontalAlignment(SwingConstants.CENTER);
        close=new JButton("关闭");
        close.setHorizontalAlignment(SwingConstants.CENTER);

        p3.add(new JLabel());
        p3.add(new JLabel());
        p3.add(loginbt);
        p3.add(close);
        p3.add(new JLabel());
        p3.add(new JLabel());

        JPanel p4=new JPanel(new GridLayout(1,3,1,1));
        signin=new JLabel("注册用户名和密码");
        signin.setForeground(Color.blue);
        signin.setHorizontalAlignment(SwingConstants.RIGHT);
        p4.add(new JPanel());
        p4.add(signin);
        p4.add(new JPanel());

        JPanel panel=new JPanel(new GridLayout(7, 1,10,20));
        for (int i=0;i<3;i++){
            panel.add(new JPanel());
        }
        panel.add(p1);
        panel.add(p2);
        panel.add(p4);
        panel.add(p3);


        frame=new JFrame();
        frame.setTitle("Consmitor远程控制");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame. add(panel);

        frame.setLayout(new FlowLayout());
        frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
        frame.setLocationRelativeTo(null);
        frame. setVisible(true);

        dialog=DialogFactory(dialog,frame,"登录成功！");
        login_Error_dialog=DialogFactory(login_Error_dialog,frame,"登录失败，请重新上线!");
        nameError_dialog=DialogFactory(nameError_dialog,frame,"用户名或密码错误!");
        noSigninDialog= DialogFactory(noSigninDialog,frame,"您未注册！");
        signin_error_Dialog=DialogFactory(signin_error_Dialog,frame,"请输入正确的姓名密码!");
        initSigninDialog(frame);
    }

    private void initSigninDialog(JFrame frame) {
        signinDialog=new Dialog(frame,"提示",false);
        signinDialog.setBounds((d.width - width)/2+height/3, (d.height - height)/2 +50, width/2, height/2);
        signinDialog.setLayout(new FlowLayout());

        JPanel panel_singup=new JPanel(new GridLayout(6,1,5,5));
        username_signin=new JTextField(15);
        panel_singup.add(new JLabel("用户名:"));
        panel_singup.add(username_signin);

        pwd_signin=new JPasswordField(15);
        panel_singup.add(new JLabel("密码:"));
        panel_singup.add(pwd_signin);

        ok_sigin=new JButton("确定");
        ok_sigin.setHorizontalAlignment(SwingConstants.CENTER);
        panel_singup.add(new JPanel());
        panel_singup.add(ok_sigin);
        signinDialog.setLayout(new FlowLayout());
        signinDialog.add(panel_singup);
    }


    private Dialog DialogFactory(Dialog dl,JFrame frame,String t) {
        dl=new Dialog(frame,"提示",false);
        dl.setBounds((d.width - width)/2+(height/2), (d.height - height)/2+50 , width/4, height/4);
        dl.setLayout(new FlowLayout());
        JLabel s=new JLabel(t);
        dl.add(s);
        return dl;
    }

    private void initClick() {
        dialogClick(dialog);
        dialogClick(signinDialog);
        dialogClick(noSigninDialog);
        dialogClick(login_Error_dialog);
        dialogClick(nameError_dialog);
        loginbt.addActionListener(e -> {
            String u=username.getText();
            String p= String.valueOf(pwd.getPassword());
            if (info[0]==null | info[0].equals("")){
                noSigninDialog.setVisible(true);
            }else{
                if (u.equals(info[0]) & p.equals(info[1])){
                    if (OpenService.startSocket(u,p)){
                        dialog.setVisible(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OpenService.loop();
                            }
                        }).start();
                    }
                    else
                        login_Error_dialog.setVisible(true);
                }else
                    nameError_dialog.setVisible(true);
            }
        });

        close.addActionListener( e -> {//关闭
            System.exit(0);
           // OpenService.closeSocket();
        });

        signin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signinDialog.setVisible(true);
            }
        });

        ok_sigin.addActionListener(e -> {
            String u=username_signin.getText();
            String p=String.valueOf(pwd_signin.getPassword());
            if (u.equals("")| p.equals("")){
                signin_error_Dialog.setVisible(true);
            }else{
                SaveInfo.save(username_signin.getText(), String.valueOf(pwd_signin.getPassword()));
                info=SaveInfo.getinfo();
                signinDialog.setVisible(false);
            }
        });
    }

    private void dialogClick(Dialog dialog) {
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.setVisible(false);
            }
        });
    }
}
