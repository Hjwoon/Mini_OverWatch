import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;

import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class BlockGameFrame extends JFrame {
    private Clip clip;
    BlockGameFrame() {
        setSize(800,600);
        setTitle("OVER WATCH");
        menuBar();

        setContentPane(new BlockGamePanel());

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void menuBar() {
        // 메뉴바
        JMenuBar mb = new JMenuBar();

        JMenu openMenu = new JMenu("OPEN");
        JMenuItem openItem = new JMenuItem("OPEN FILE");

        // Open 메뉴 아이템에 Action 리스너 등록
        openItem.addActionListener(new OpenActionListener());
        openMenu.add(openItem);

        JMenu fileMenu = new JMenu("FILE");
        fileMenu.add(new JMenuItem("ADD"));
        fileMenu.add(new JMenuItem("DELETE"));

        JMenu soundMenu = new JMenu("MUSIC");
        JMenuItem onItem = new JMenuItem("ON");
        soundMenu.add(onItem);
        soundMenu.addSeparator();
        JMenuItem offItem = new JMenuItem("OFF");
        soundMenu.add(offItem);
        // Music 메뉴 아이템에 Action 리스너 등록
        onItem.addActionListener(new MusicOnActionListener());
        offItem.addActionListener(new MusicOffActionListener());

        JMenu exitMenu = new JMenu("END GAME");
        JMenuItem exitItem = new JMenuItem("EXIT");

        // exit 메뉴 아이템에 Action 리스너 등록
        exitItem.addActionListener(new ExitActionListener());
        exitMenu.add(exitItem);

        mb.add(openMenu);
        mb.add(fileMenu);
        mb.add(soundMenu);
        mb.add(exitMenu);

        setJMenuBar(mb);
    }

    class OpenActionListener implements ActionListener {
        private JFileChooser chooser;

        public OpenActionListener() {
            chooser = new JFileChooser(); // 파일 다이얼로그 생성
            // 초기 디렉터리 설정 (현재 작업 디렉터리로 설정)
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        }

        public void actionPerformed(ActionEvent e) {
           FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "XML Files",
                    "xml"); // 파일 필터로 사용되는 확장자. *.xml만 나열됨

            chooser.setFileFilter(filter);

            int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
            if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                        "경고", JOptionPane.WARNING_MESSAGE);

                return;
            }

            // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
            String filePath = chooser.getSelectedFile().getPath(); // 파일 경로명 리턴*/
            XMLReader xml = new XMLReader(filePath);
            Node blockGameNode = xml.getBlockGameElement();
            Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SIZE);
            String w = XMLReader.getAttr(sizeNode, "w");

            String h = XMLReader.getAttr(sizeNode, "h");
            setSize(Integer.parseInt(w), Integer.parseInt(h));

            GamePanel gamePanel = new GamePanel(xml.getGamePanelElement(), Integer.parseInt(w), Integer.parseInt(h)); // GamePanel에서 obsTh가 접근
            setContentPane(gamePanel);


            setVisible(true);
            gamePanel.setFocus();
        }
    }

    class MusicOnActionListener implements ActionListener {
        MusicOnActionListener() {}
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        File file = new File("firstBGM.wav");
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                        clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }

    class MusicOffActionListener implements ActionListener {
        MusicOffActionListener() {}

        @Override
        public void actionPerformed(ActionEvent e) {
            // 음악 중지 및 리소스 해제
            stopMusic();
        }

        // 음악 중지 및 리소스 해제를 위한 메서드
        private void stopMusic() {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
    }

    class ExitActionListener implements ActionListener {
        ExitActionListener() {}

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(BlockGameFrame.this,
                    "정말 종료하시겠어요?", "종료 확인", JOptionPane.YES_NO_OPTION);


            if (result == JOptionPane.YES_OPTION) {
                // 예 버튼을 눌렀을 때 프로그램 종료
                System.exit(0);
            } else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
                // 아니오 버튼이나 창을 닫기 버튼을 눌렀을 때는 아무 동작도 수행하지 않음
            }
        }
    }
}

class BlockGamePanel extends JPanel {
    private ImageIcon icon = new ImageIcon("first.jpg");
    private Image img = icon.getImage();
    private JLabel playLa = new JLabel("START");
    private JLabel scoreLa = new JLabel("SCORE");
    private JLabel introLa = new JLabel("INTRO");

    BlockGamePanel() {
        LabelMouseListener listener = new LabelMouseListener(); // 리스너 객체 생성
        playLa.addMouseListener(listener); // MouseListener 리스너 등록
        playLa.addMouseMotionListener(listener); // MouseMotionListener 리스너 등록

        setLayout(null);
        playLa.setBounds(20,60,300, 100);
        Font font = new Font("Elephant", Font.BOLD, 40);
        playLa.setFont(font);
        playLa.setForeground(Color.WHITE);
        add(playLa);

        scoreLa.addMouseListener(listener); // MouseListener 리스너 등록
        scoreLa.addMouseMotionListener(listener); // MouseMotionListener 리스너 등록

        scoreLa.setBounds(20, 160,300, 100);
        scoreLa.setFont(font);
        scoreLa.setForeground(Color.WHITE);
        add(scoreLa);

        introLa.addMouseListener(listener); // MouseListener 리스너 등록
        introLa.addMouseMotionListener(listener); // MouseMotionListener 리스너 등록

        introLa.setBounds(20, 260,300, 100);
        introLa.setFont(font);
        introLa.setForeground(Color.WHITE);
        add(introLa);
    }

    class LabelMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() instanceof JLabel) {
                JLabel label = (JLabel) e.getSource();

                if (label == playLa) {
                    // 클릭한 라벨이 "START" 라벨인지 확인
                    System.out.println("pressed!");
                } else if (label == scoreLa) {
                    // 다른 라벨에 대한 처리
                } else if (label == introLa) {
                    // 다른 라벨에 대한 처리
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(),null);
    }
}