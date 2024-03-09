import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.Vector;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AuthorFrame extends JFrame { // 저작도구
    private int gWidth, gHeight;
    private SettingPanel settingPanel = new SettingPanel();
    private GameGround gameGround = new GameGround();
    private ImageIcon settingPanelBG = new ImageIcon("nightsky.jpg");
    String filePath = null;
    private ImageIcon bgImg = null;
    private String playerFileName, enemyFileName, audioFileName, obj1FileName, obj2FileName, coinFileName; // Player, enemy, WAV, ... 등 파일 경로를 저장할 변수
    private ImageIcon playerImg = null;
    private ImageIcon enemyImg = null;
    private ImageIcon obs1Img = null;
    private ImageIcon obs2Img = null;
    private ImageIcon coinImg = null;
    private JLabel coinSetText = new JLabel("설정");
    private JLabel coinCopyText = new JLabel("복사");
    private Vector<cBlock> cBlocks = new Vector<>(); // 모든 coin 블록 저장
    JPanel cPopPanel;
    private JTextField sizeFW, sizeFH, lifeF;
    private int sizeW, sizeH, life;
    private Clipboard clipboard;
    private cBlock lastClickedBlock = null;
    private JTextField bX, bY, bWidth, bHeight, bLife; // 블록 속성 텍스트 필드

    public AuthorFrame() {

        setSize(1000,600);
        setTitle("저작도구");

        setLayout(new BorderLayout());

        JSplitPane sPane = new JSplitPane();
        sPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        sPane.setDividerLocation(700);

        sPane.setLeftComponent(gameGround);
        sPane.setRightComponent(settingPanel);
        sPane.setEnabled(false);

        add(sPane);

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();  // clipboard 초기화

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public class SettingPanel extends JPanel {
        private JFileChooser chooser;

        public SettingPanel() {
            setBackground(new Color(0xB99ACE));
            setLayout(null);

            JLabel bgLabel = new JLabel("BG :");
            JLabel playerLabel = new JLabel("Player :");
            JLabel enemyLabel = new JLabel("Enemy :");
            JLabel obs1Label = new JLabel("Obs :");
            JLabel blockLabel = new JLabel("Block :");
            JLabel bXLabel = new JLabel("x좌표:");
            JLabel bYLabel = new JLabel("y좌표:");
            JLabel bWidthLabel = new JLabel("너비:");
            JLabel bHeightLabel = new JLabel("높이:");
            JLabel bLifeLabel = new JLabel("Life:");
            
            JButton bgButton = new JButton("+사진 선택");
            JButton bgSoundButton = new JButton("BGM");
            JButton playerButton = new JButton("+사진 선택");
            JButton enemyButton = new JButton("+사진 선택");
            JButton obs1Button = new JButton("+사진 선택");
            JButton obs2Button = new JButton("+사진 선택");
            JButton bSettingSave = new JButton("save");

            JLabel coinLa = new JLabel("coin ");
            JButton coinBtn = new JButton("생성");
            
            // 블록 속성 텍스트 필드
            bX = new JTextField(4);
            bY = new JTextField(4);
            bWidth = new JTextField(4);
            bHeight = new JTextField(4);
            bLife = new JTextField(3);

            // 팝업에 들어갈 컴포넌트들을 담을 패널 생성
            cPopPanel = new JPanel();
            sizeFW = new JTextField(4);
            sizeFH = new JTextField(4);
            lifeF = new JTextField(2);

            cPopPanel.add(new JLabel("Size :"));
            cPopPanel.add(sizeFW);
            cPopPanel.add(new JLabel(" x "));
            cPopPanel.add(sizeFH);
            cPopPanel.add(new JLabel("Life :"));
            cPopPanel.add(lifeF);

            chooser = new JFileChooser(); // 파일 다이얼로그 생성
            // 초기 디렉터리 설정 (현재 작업 디렉터리로 설정)
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

            // JButton 이벤트 처리
            bgButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("bButton Clicked!");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files",
                            "png", "jpg"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                                "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                    filePath = chooser.getSelectedFile().getName(); // 파일 경로명 리턴
                    bgImg = new ImageIcon(filePath);
                    gameGround.repaint();
                }
            });
            playerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("pButton Clicked!");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files",
                            "png", "jpg"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                                "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                    playerFileName = chooser.getSelectedFile().getName(); // 파일 경로명 리턴
                    playerImg = new ImageIcon(playerFileName);
                    gameGround.repaint();
                }
            });
            enemyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("eButton Clicked!");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files",
                            "png", "jpg"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                                "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                    enemyFileName = chooser.getSelectedFile().getName(); // 파일 경로명 리턴
                    enemyImg = new ImageIcon(enemyFileName);
                    gameGround.repaint();
                }
            });
            obs1Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("o1Button Clicked!");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files",
                            "png", "jpg"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                                "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                    obj1FileName = chooser.getSelectedFile().getName(); // 파일 경로명 리턴
                    obs1Img = new ImageIcon(obj1FileName);
                    gameGround.repaint();
                }
            });
            obs2Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("o2Button Clicked!");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files",
                            "png", "jpg"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                                "경고", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                    obj2FileName = chooser.getSelectedFile().getName(); // 파일 경로명 리턴
                    obs2Img = new ImageIcon(obj2FileName);
                    gameGround.repaint();
                }
            });
            coinBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("cButton Clicked!");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files",
                            "png", "jpg"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret != JFileChooser.APPROVE_OPTION) { // 사용자가 창을 강제로 닫았거나 취소 버튼을 누르는 경우
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않음",
                                "블록 생성을 취소하시겠습니까?", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                    coinFileName = chooser.getSelectedFile().getName(); // 파일 경로명 리턴
                    coinImg = new ImageIcon(coinFileName);

                    coinSetText.setBounds(210, 275, 60, 15);
                    coinSetText.addMouseListener(new CoinImageMouseListener());
                    coinSetText.setForeground(Color.WHITE);

                    coinCopyText.setBounds(210, 295, 60, 10);
                    coinCopyText.addMouseListener(new CoinImageMouseListener());
                    coinCopyText.setForeground(Color.WHITE);

                    // SettingPanel에 추가
                    add(coinSetText);
                    add(coinCopyText);
                    settingPanel.repaint();
                }
            });
            bgSoundButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    // 파일 선택기의 기본 디렉토리를 현재 작업 디렉토리로 설정
                    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Audio Files",
                            "wav"); // 파일 필터로 사용되는 확장자

                    chooser.setFileFilter(filter);

                    int ret = chooser.showOpenDialog(null); // 파일 다이얼로그 출력
                    if (ret == JFileChooser.APPROVE_OPTION) { // 사용자가 파일을 선택한 경우
                        // 사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
                        File selectedFile = chooser.getSelectedFile();
                        audioFileName = selectedFile.getName();
                    } else {
                        JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다",
                                "경고", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            bgLabel.setBounds(30, 20, 60, 40);
            bgButton.setBounds(80, 20, 100, 40);
            playerLabel.setBounds(30, 70, 60, 40);
            playerButton.setBounds(80, 70, 100, 40);
            enemyLabel.setBounds(30, 120, 60, 40);
            enemyButton.setBounds(80, 120, 100, 40);
            obs1Label.setBounds(30, 170, 60, 40);
            obs1Button.setBounds(80, 170, 100, 40);
            obs2Button.setBounds(80, 220, 100, 40);
            blockLabel.setBounds(30, 270, 60, 40);
            coinLa.setBounds(80, 270, 60, 40);
            coinBtn.setBounds(110, 270, 60, 40);
            bgSoundButton.setBounds(190, 20, 70, 40);
            bXLabel.setBounds(30, 330, 40, 30);
            bX.setBounds(80, 330, 40, 30);
            bYLabel.setBounds(150, 330, 40, 30);
            bY.setBounds(200, 330, 40, 30);
            bWidthLabel.setBounds(10, 370, 40, 30);
            bWidth.setBounds(40, 370, 50, 30);
            bHeightLabel.setBounds(100, 370, 40, 30);
            bHeight.setBounds(130, 370, 50, 30);
            bLifeLabel.setBounds(190, 370, 40, 30);
            bLife.setBounds(220, 370, 40, 30);
            bSettingSave.setBounds(110, 420, 70, 30);

            bgLabel.setForeground(Color.WHITE);
            playerLabel.setForeground(Color.WHITE);
            enemyLabel.setForeground(Color.WHITE);
            obs1Label.setForeground(Color.WHITE);
            blockLabel.setForeground(Color.WHITE);
            coinLa.setForeground(Color.WHITE);
            bXLabel.setForeground(Color.WHITE);
            bYLabel.setForeground(Color.WHITE);
            bWidthLabel.setForeground(Color.WHITE);
            bHeightLabel.setForeground(Color.WHITE);
            bLifeLabel.setForeground(Color.WHITE);

            add(bgLabel);
            add(bgButton);
            add(bgSoundButton);
            add(playerLabel);
            add(playerButton);
            add(enemyLabel);
            add(enemyButton);
            add(obs1Label);
            add(obs1Button);
            add(obs2Button);
            add(blockLabel);
            add(coinLa);
            add(coinBtn);
            add(bXLabel);
            add(bX);
            add(bYLabel);
            add(bY);
            add(bWidthLabel);
            add(bWidth);
            add(bHeightLabel);
            add(bHeight);
            add(bLifeLabel);
            add(bLife);
            add(bSettingSave);


            // "저장" 버튼 추가
            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField widthField = new JTextField();
                    JTextField heightField = new JTextField();
                    Object[] inputFields = {"Width:", widthField, "Height:", heightField};

                    int result = JOptionPane.showOptionDialog(null, inputFields, "게임 설정 저장",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                    // "게임 저장"을 선택한 경우
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            // 입력 받은 가로와 세로 크기를 정수로 변환
                            gWidth = Integer.parseInt(widthField.getText());
                            gHeight = Integer.parseInt(heightField.getText());

                            // 설정 저장 메소드 호출
                            saveSettingsToXml();
                        } catch (NumberFormatException ex) {
                            // 숫자로 변환할 수 없는 값이 입력된 경우 예외 처리
                            JOptionPane.showMessageDialog(null, "올바른 숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            // "블록 속성 저장" 버튼 추가
            bSettingSave.addActionListener(new ActionListener() {
            	@Override
            	public void actionPerformed(ActionEvent e) {
            		lastClickedBlock.setX(Integer.parseInt(bX.getText()));
            		lastClickedBlock.setY(Integer.parseInt(bY.getText()));
            		lastClickedBlock.setWidth(Integer.parseInt(bWidth.getText()));
            		lastClickedBlock.setHeight(Integer.parseInt(bHeight.getText()));
            		lastClickedBlock.setLife(Integer.parseInt(bLife.getText()));
            		repaint();
            		gameGround.repaint(); // 게임 패널도 다시 그리기
            		
            	}
            });

            saveButton.setBounds(110, 500, 60, 40);
            saveButton.setForeground(Color.BLACK);
            add(saveButton);
        }

        private void saveSettingsToXml() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("게임 설정 저장");
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML 파일", "xml"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".xml")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".xml");
                }

                try (FileWriter writer = new FileWriter(fileToSave)) {
                    writer.write("<?xml version=\"1.0\" encoding=\"euc-kr\" ?>\n");
                    writer.write("<BlockGame>\n");
                    writer.write("    <Screen>\n");
                    writer.write("        <Size w=\"" + gWidth + "\" h=\"" + gHeight + "\"></Size>\n");
                    writer.write("    </Screen>\n");

                    writer.write("    <GamePanel>\n");
                    writer.write("        <BG>\n");
                    writer.write("            <BgIMG>" + ((bgImg != null) ? filePath : "") + "</BgIMG>\n");
                    writer.write("            <BgBGM>" + ((audioFileName != null) ? audioFileName : "") + "</BgBGM>\n");
                    writer.write("        </BG>\n");

                    writer.write("        <Player>\n");
                    writer.write("            <!-- player -->\n");
                    writer.write("            <Obj x=\"" + (gWidth/2 - 30) + "\" y=\"" + (int)(gHeight*0.9) + "\" w=\"35\" h=\"50\" life=\"100\" img=\"" + playerFileName + "\"></Obj>\n");
                    writer.write("        </Player>\n");

                    writer.write("        <Skill>\n");
                    writer.write("            <!-- skills -->\n");
                    writer.write("            <Obj x=\"5\" y=\"420\" w=\"30\" h=\"30\" type=\"1\" img=\"heal.png\"></Obj>\n");
                    writer.write("            <Obj x=\"40\" y=\"420\" w=\"30\" h=\"30\" type=\"2\" img=\"wave.png\"/>\n");
                    writer.write("            <Obj x=\"75\" y=\"420\" w=\"30\" h=\"30\" type=\"3\" img=\"amp.png\"/>\n");
                    writer.write("            <Obj x=\"550\" y=\"420\" w=\"30\" h=\"30\" type=\"0\" img=\"lucioQ.png\"/>\n");
                    writer.write("        </Skill>\n");

                    writer.write("        <Obstacle>\n");
                    writer.write("            <Obj x=\"120\" y=\"240\" w=\"40\" h=\"50\" img=\"" + obj1FileName + "\"></Obj>\n");
                    writer.write("            <Obj x=\"250\" y=\"270\" w=\"40\" h=\"50\" img=\"" + obj2FileName + "\"></Obj>\n");
                    writer.write("        </Obstacle>\n");

                    writer.write("        <Block>\n");
                    for (cBlock block : cBlocks) { // 저장된 벡터 안의 블록들을 텍스트로 출력
                        writer.write(block.info());
                    }
                    writer.write("        </Block>\n");

                    writer.write("        <Enemy>\n");
                    writer.write("            <Obj x=\"" + (gWidth/2 - 30) + "\" y=\"" + (gHeight*0) + "\" w=\"35\" h=\"50\" life=\"50\" img=\"" + enemyFileName + "\"></Obj>\n");
                    writer.write("        </Enemy>\n");
                    writer.write("    </GamePanel>\n");
                    writer.write("</BlockGame>\n");

                    JOptionPane.showMessageDialog(null, "게임이 성공적으로 저장되었습니다", "성공", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "게임 저장 중 오류가 발생했습니다", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private String getImageFileName(cBlock block) {
            if (block != null) {
                return block.getBlockImgName();
            }
            // cBlock이 null이면 빈 문자열 반환
            return "";
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(settingPanelBG.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            if(coinImg != null)
                g.drawImage(coinImg.getImage(), 180, 275, 30, 30, this);

        }
    }

    private class cBlock {
        private ImageIcon imageIcon;
        private String blockImgName;
        private int x, y, width, height, life;
        private String info;


        public cBlock(ImageIcon imageIcon, int x, int y, int width, int height, int life) {
            this.imageIcon = imageIcon;
            this.blockImgName = blockImgName;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.life = life;
            this.info="        <Obj x=\"" + x + "\" y=\"" + y +
                    "\" w=\"" + width + "\" h=\"" + height +
                    "\" life=\"" + life + "\" img=\"" + blockImgName + "\"></Obj>\n";
        }

        public ImageIcon getImageIcon() {
            return imageIcon;
        }

        public String getBlockImgName() { return blockImgName; }

        public int getX() { return x; }
        
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }
        
        public void setY(int y) { this.y = y; }

        public int getWidth() { return width; }
        
        public void setWidth(int width) { this.width = width; }

        public int getHeight() { return height; }
        
        public void setHeight(int height) { this.height = height; }

        public int getLife() { return life; }
        
        public void setLife(int life) { this.life = life; }

        public void setLocation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String info() {
            return info;
        }

        public void writeInfo() {
            this.info="        <Obj x=\"" + x + "\" y=\"" + y +
                    "\" w=\"" + width + "\" h=\"" + height +
                    "\" life=\"" + life + "\" img=\"" + blockImgName + "\"></Obj>\n";
        }

        // 이미지를 그리는 메서드
        public void draw(Graphics g) {
            g.drawImage(imageIcon.getImage(), x, y, width, height, null);
        }
    }

    private class CoinImageMouseListener extends MouseAdapter {
        private int offsetX, offsetY;

        @Override
        public void mousePressed(MouseEvent e) {
            offsetX = e.getX();
            offsetY = e.getY();

            // "설정" 레이블일 경우
            if ("설정".equals(((JLabel) e.getSource()).getText())) {
                // 팝업창 생성
                // 사용자에게 옵션을 선택하도록 메시지 다이얼로그 표시
                Object[] options = {"생성", "취소"};
                int result = JOptionPane.showOptionDialog(null, cPopPanel, "블록 속성 설정",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                // 사용자가 "생성"을 선택한 경우
                if (result == JOptionPane.OK_OPTION) {
                    // 생성에 필요한 변수들을 클래스 멤버 변수에 저장
                    sizeW = Integer.parseInt(sizeFW.getText());
                    sizeH = Integer.parseInt(sizeFH.getText());
                    life = Integer.parseInt(lifeF.getText());
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            // "복사" 레이블일 경우
            else if ("복사".equals(((JLabel) e.getSource()).getText())) {
                // 이미지를 TransferableImage로 포장
                TransferableImage transferableImage = new TransferableImage(coinImg.getImage());
                // 클립보드에 이미지 설정
                clipboard.setContents(transferableImage, null);
                // 클립보드의 내용을 콘솔에 출력 (디버깅용)
                System.out.println(clipboard.getContents(clipboard));
            }
        }
    }

    // TransferableImage 클래스 정의 (Transferable을 구현)
    public class TransferableImage implements Transferable {

        private Image image;

        // 생성자
        public TransferableImage(Image image) {
            this.image = image;
        }

        // Transferable 인터페이스의 메서드 구현
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (DataFlavor.imageFlavor.equals(flavor)) {
                return image;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }

    public class GameGround extends JPanel {
        private int draggedX, draggedY; // 드래그 중인 이미지의 좌표

        public GameGround() {
            // 마우스 리스너 및 모션 리스너 추가
            addMouseListener(new GameGroundMouseListener());
            addMouseMotionListener(new GameGroundMouseMotionListener());
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (bgImg != null)
                g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            if (playerImg != null)
                g.drawImage(playerImg.getImage(), this.getWidth() / 2 - 30, (int) (this.getHeight() * 0.9), 35, 50, this);
            if (enemyImg != null)
                g.drawImage(enemyImg.getImage(), this.getWidth() / 2 - 30, this.getHeight() * 0, 35, 50, this);
            if (obs1Img != null)
                g.drawImage(obs1Img.getImage(), (int) (this.getWidth() * 0), (int) (this.getWidth() * 0.4), 60, 50, this);
            if (obs2Img != null)
                g.drawImage(obs2Img.getImage(), (int) (this.getWidth() * 0.3), (int) (this.getWidth() * 0.5), 60, 50, this);

            // 코인 블록 그리기
            for (cBlock block : cBlocks) {
                block.draw(g);
            }

            // 드래그 중인 이미지 그리기
            if (lastClickedBlock != null) {
                lastClickedBlock.draw(g);
                g.setColor(Color.black);
                g.drawRect(lastClickedBlock.getX(), lastClickedBlock.getY(), lastClickedBlock.getWidth(), lastClickedBlock.getHeight());
                bX.setText(Integer.toString(lastClickedBlock.getX()));
                bY.setText(Integer.toString(lastClickedBlock.getY()));
                bWidth.setText(Integer.toString(lastClickedBlock.getWidth()));
                bHeight.setText(Integer.toString(lastClickedBlock.getHeight()));
                bLife.setText(Integer.toString(lastClickedBlock.getLife()));
            }
        }

        private class GameGroundMouseListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.isMetaDown()) { // 우클릭 했을 때 메뉴 나오게
                    if(lastClickedBlock != null) {
                        showPopupMenu(e);
                    }
                }
                else { // 좌클릭 했을 때
                    // 클릭한 위치의 좌표
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    // 클립보드에서 이미지를 가져오기
                    Transferable contents = clipboard.getContents(null);
                    if (contents != null && contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                        try {
                            Image copiedImage = (Image) contents.getTransferData(DataFlavor.imageFlavor);

                            // 블록 추가
                            lastClickedBlock = new cBlock(new ImageIcon(copiedImage), mouseX, mouseY, sizeW, sizeH, life);
                            cBlocks.add(lastClickedBlock);

                            repaint(); // 화면 다시 그리기
                        } catch (UnsupportedFlavorException | IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // 마우스를 누른 위치의 좌표
                int pressedX = e.getX();
                int pressedY = e.getY();

                // 마우스를 누른 위치에 있는 블록을 찾기
                for (cBlock block : cBlocks) {
                    if (pressedX >= block.getX() && pressedX <= block.getX() + block.getWidth() &&
                            pressedY >= block.getY() && pressedY <= block.getY() + block.getHeight()) {
                        // 블록을 클릭한 것으로 설정
                        lastClickedBlock = block;
                        // 현재 위치 저장
                        draggedX = pressedX - block.getX();
                        draggedY = pressedY - block.getY();
                        break;
                    }
                }
            }
        }

        private class GameGroundMouseMotionListener extends MouseMotionAdapter {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 드래그 중인 이미지를 찾아서 좌표 업데이트
                if (lastClickedBlock != null) {
                    lastClickedBlock.setLocation(e.getX(), e.getY());
                    lastClickedBlock.writeInfo();
                    repaint(); // 화면 다시 그리기
                }
            }
        }


        private void showPopupMenu(MouseEvent e) {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem deleteBlockItem = new JMenuItem("삭제");
            deleteBlockItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Delete();
                }
            });
            
            popupMenu.add(deleteBlockItem);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

        private void Delete() { // 블록 삭제 기능
            if(cBlocks != null) {
                for(int i = 0; i < cBlocks.size(); i++) {
                    if(cBlocks.get(i) == lastClickedBlock) {
                    	System.out.println(cBlocks.size());
                        cBlocks.remove(i);
                        lastClickedBlock = null;
                        bX.setText("");
                        bY.setText("");
                        bWidth.setText("");
                        bHeight.setText("");
                        repaint();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new AuthorFrame();
    }
}