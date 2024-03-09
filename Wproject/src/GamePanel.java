import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

class GamePanel extends JPanel {
    ImageIcon bgImg;
    Player player;
    Enemy enemy;
    Vector<Block> blList = new Vector<>();
    Obs obs;
    Vector<Obs> obsList = new Vector<>();
    JLabel bulletLabel = new JLabel(); // 총알
    JLabel ebulletLabel = new JLabel(); // 몬스터 총알
    public GamePanel(Node gamePanelNode, int panelWidth, int panelHeight) {
        setLayout(null);
        Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BGIMG);
        bgImg = new ImageIcon(bgNode.getTextContent());

        // read <Fish><Obj>s from the XML parse tree, make Food objects, and add them to the FishBowl panel.
        Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
        Node playerNode = XMLReader.getNode(gamePanelNode, XMLReader.E_PLAYER);
        Node skillNode = XMLReader.getNode(gamePanelNode, XMLReader.E_SKILL);
        Node obsNode = XMLReader.getNode(gamePanelNode, XMLReader.E_OBS);
        Node enemyNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ENEMY);

        // 플레이어
        NodeList playerNodeList = playerNode.getChildNodes();
        for(int i=0; i<playerNodeList.getLength(); i++) {
            Node node = playerNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {
                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                int life = Integer.parseInt(XMLReader.getAttr(node, "life"));

                ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                player = new Player(x,y,w,h,life,icon);
                add(player);

                PlayerThread pTh = new PlayerThread(player);
                pTh.start();
            }
        }

        // 스킬
        NodeList skillNodeList = skillNode.getChildNodes();
        for(int i=0; i<skillNodeList.getLength(); i++) {
            Node node = skillNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {
                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                int type = Integer.parseInt(XMLReader.getAttr(node, "type"));

                ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                Skill skill = new Skill(x,y,w,h,type,icon);
                add(skill);
            }
        }

        // 장애물
        NodeList obsNodeList = obsNode.getChildNodes();
        for(int i=0; i<obsNodeList.getLength(); i++) {
            Node node = obsNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {
                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));

                ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                obs = new Obs(x,y,w,h,icon);
                add(obs);
                obsList.add(obs);

                ObsThread oTh = new ObsThread(obs,panelWidth);
                oTh.start();
            }
        }

        // 블록
        NodeList blockNodeList = blockNode.getChildNodes();
        for(int i=0; i<blockNodeList.getLength(); i++) {
            Node node = blockNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {
                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                int type = Integer.parseInt(XMLReader.getAttr(node, "type"));
                int life = Integer.parseInt(XMLReader.getAttr(node, "life"));

                ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                Block block = new Block(x,y,w,h,type,life,icon);
                add(block);
                blList.add(block);
            }
        }

        // 몬스터
        NodeList enemyNodeList = enemyNode.getChildNodes();
        for(int i=0; i<enemyNodeList.getLength(); i++) {
            Node node = enemyNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {
                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                int life = Integer.parseInt(XMLReader.getAttr(node, "life"));

                ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                enemy = new Enemy(x,y,w,h,life,icon);
                add(enemy);

                EnemyThread eTh = new EnemyThread(enemy, panelWidth, panelHeight);
                eTh.start();
            }
        }

        bulletLabel.setSize(10,10);
        bulletLabel.setVisible(false);
        bulletLabel.setOpaque(true);
        bulletLabel.setBackground(Color.GREEN);
        add(bulletLabel);

        ebulletLabel.setSize(10,10);
        ebulletLabel.setVisible(false);
        ebulletLabel.setOpaque(true);
        ebulletLabel.setBackground(Color.RED);
        add(ebulletLabel);
    }

    public void setFocus() {
        player.setFocusable(true);
        player.requestFocus();
    }

    class Player extends JLabel{
        Image img;
        int x, y, w, h;
        private int life;  // 플레이어의 체력
        private int maxLife; // 플레이어의 최대 체력

        public int getW() { return w; }
        public Player(int x, int y, int w, int h, int life, ImageIcon icon) {
            this.setBounds(x,y,w,h+20);
            img = icon.getImage();
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.life = life; // 초기 HP
            this.maxLife = life;
        }

        // 플레이어 체력 감소 메서드
        public void decreaseHealth(int damage) {
            life -= damage;
            if (life < 0) {
                System.exit(0);// 게임 오버 처리
            }
        }

        // 플레이어 체력 증량 메서드
        public void increaseHealth(int extraLife) {
            life += extraLife;
            if (life > 100) {
                life = 100;
            }
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight()-20, this);
            g.setColor(Color.GREEN);
            g.drawRect(0, 55, this.getW()-1, 14);
            g.fillRect(0,55,(getW()-1)*life/maxLife,14);
        }
    }

    class Skill extends JLabel{
        Image img;
        private int type;


        public Skill() {
            //
        }

        public Skill(int x, int y, int w, int h, int type, ImageIcon icon) {
            this.setBounds(x,y,w,h);
            img = icon.getImage();
            this.type=type;

            SkillTimeThread sTh = new SkillTimeThread();
            sTh.start();
        }

        class SkillTimeThread extends Thread { // 스킬 쿨타임 스레드
            private static int sharedCooldown = 20; // 공유되는 쿨타임 (초)
            private int remainingCooldown; // 남은 쿨타임 (초)
            private JLabel cooldownLabel = new JLabel("");; // 쿨타임을 표시하는 레이블
            private boolean cooldownTime; // 쿨타임 여부

            public SkillTimeThread() {
                System.out.println("HI");
                // 두번째 skill label 상단에 쿨타임 레이블 부착
                cooldownLabel.setBounds(5, this.cooldownLabel.getHeight(), 15, 15);
                cooldownLabel.setForeground(Color.RED);
                cooldownLabel.setFont(new Font("Arial", Font.BOLD, 16));

                add(cooldownLabel);
            }
        } // 스킬 구현하기

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        public void skill1() { // 체력 15 회복
            player.increaseHealth(15);
            player.repaint();
        }

        /*public void useSkill() {
            if (remainingCooldown > 0) {
                System.out.println("스킬 " + type + " 쿨타임 중입니다. 남은 시간: " + remainingCooldown + "초");
                return;
            }

            // 각 스킬에 따른 동작 추가
            switch (type) {
                case 1: // 체력 15 회복
                    skill1();
                    break;
                case 2:
                    //skill2();
                    break;
                case 3:
                    //skill3();
                    break;
                case 4:
                    //skillQ();
                    break;
            }

            // 쿨타임 시작
            startCooldown();
        */

        /*private void startCooldown() {
            remainingCooldown = cooldownSeconds;

            // 쿨타임을 감소시키는 스레드 시작
            new Thread(new CooldownRunnable()).start();
        }*/

        /*private class CooldownRunnable implements Runnable {
            @Override
            public void run() {
                while (remainingCooldown > 0) {
                    try {
                        Thread.sleep(1000); // 1초마다 감소
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    remainingCooldown--;
                }
            }
        }*/
    }

    class Block extends JLabel {
        private int x, y, w, h, type, life;
        Image img;
        public Block(int x, int y, int w, int h, int type, int life, ImageIcon icon) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.type = type;
            this.life = life;
            this.setBounds(x,y,w,h);
            img = icon.getImage();
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public int getW() { return w; }
        public int getH() { return h; }
        public int getType() { return type; }
        public int getLife() { return life; }
        public void loseLife(int dmg) {

            this.life -= dmg;
            if(life<=0)
                this.setVisible(false);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    class Enemy extends JLabel {
        Image img;
        int x, y, w, h;
        private int life;  // 몬스터의 체력
        private int maxLife; // 몬스터의 최대 체력

        public int getW() { return w; }
        public int getLife() { return life; }

        public Enemy(int x, int y, int w, int h, int life, ImageIcon icon) {
            this.setBounds(x,y,w,h);
            img = icon.getImage();
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.life = life; // 초기 HP
            this.maxLife = life;
        }

        // 몬스터 체력 감소 메서드
        public void decreaseHealth(int damage) {
            life -= damage;
            if (life < 0) {
                System.exit(0);// 게임 오버 처리
            }
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight()-20, this);
            g.setColor(Color.RED);
            g.drawRect(0, 55, this.getW()-1, 14);
            g.fillRect(0,55,(getW()-1)*life/maxLife,14);
        }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    class PlayerThread extends Thread { // 플레이어 스레드
        private Player player;
        private BulletThread bulletTh;
        Skill skill = new Skill();
        public PlayerThread(Player player) {
            this.player = player;
            player.addKeyListener(new MyKey());
            bulletTh = new BulletThread(bulletLabel, blList, player);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    player.repaint();
                    return;
                }
            }
        }

        class MyKey extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) { // 총알 발사
                    if (bulletTh == null || !bulletTh.isAlive()) {
                        bulletTh = new BulletThread(bulletLabel, blList, player);
                        bulletTh.start();
                    }
                } else if (key == KeyEvent.VK_A) { // 왼쪽 이동
                    if (player.getX() > 0) { // 프레임 범위 내
                        player.setLocation(player.getX() - 10, player.getY());
                    }
                } else if (key == KeyEvent.VK_D) { // 오른쪽 이동
                    if (player.getX() + player.getWidth() + 10 < player.getParent().getWidth())
                        player.setLocation(player.getX() + 10, player.getY());
                } else if (key == KeyEvent.VK_S) { // 힐: 체력 30 회복 // 20초
                    skill.skill1();
                } /*else if(key == KeyEvent.VK_E) { // 스킬 궁: 일시적으로 bullet 3개 // 20초

                    }
                    else if(key == KeyEvent.VK_R) { // 초음파: 떨어지는 공 치기 // 20초

                    }*/
                //else if(key == KeyEvent.VK_Q) { // 궁극기: 표면에 있는 블록 닿음처리 -1 // 60초
                        //for(int i = 0; i < th.length; i++) {


                //}
            }
        }
    }

    class BulletThread extends Thread { // 총알 스레드
        JComponent bullet; // 총알 레이블을 저장하는 변수
        Vector<Block> blList; // 블록 리스트
        private Player player;
        public BulletThread(JComponent bullet, Vector<Block> blList, Player player) {
            this.bullet = bullet;
            this.blList = blList;
            this.player = player;
        }

        public void run() {
            bulletLabel.setLocation(player.getX() + (player.getWidth() / 2 - 2), player.getY() + 2);
            bulletLabel.setVisible(true);

            while (true) {
                int x = bullet.getX();
                int y = bullet.getY() - 10; // speed 맵 레벨로 설정. -10이 총알이 올라가는 속도와 관련있다
                int w = bullet.getWidth();
                int h = bullet.getHeight();
                Rectangle rc = new Rectangle(x, y, w, h);

                for (Block block : blList) {
                    bullet.getParent().repaint();
                    if (block.getLife() > 0 && block.isVisible()) {
                        Rectangle bl = new Rectangle(block.getX(), block.getY(), block.getW(), block.getH());
                        if (rc.intersects(bl)) {
                            if(block.getType() == 0) { // bomb 블록
                                player.decreaseHealth(15);
                            } else if(block.getType() == 3){
                                player.increaseHealth(10);
                            }
                            // 블록에 부딪혔을 때의 동작 처리
                            block.loseLife(1);
                            System.out.println("Bullet hit a block!");

                            bullet.setLocation(bullet.getX(),380);
                            // 총알이 하나의 블록에만 충돌하도록 break 추가
                            return; // thread ends
                        }
                    }
                }

                for(Obs obs : obsList) {
                    if ((((bullet.getX() >= obs.getX()) && (bullet.getX() <= (obs.getX() + obs.getWidth()))) &&
                            (bullet.getY() <= (obs.getY() + obs.getHeight())))) { // 장애물에 닿았을 때
                        System.out.println("아야. 장애물에 닿았어요");
                        bulletLabel.setVisible(false); // 총알 지우기
                        return; // thread ends
                    }
                }

                if((((bullet.getX() >= enemy.getX()) && (bullet.getX() <= (enemy.getX() + enemy.getWidth()))) &&
                        (bullet.getY() <= (enemy.getY() + enemy.getHeight())))) { // 몬스터에 닿았을 때
                    enemy.decreaseHealth(5);
                    System.out.println("아야. 몬스터에 닿았어요");
                    bulletLabel.setVisible(false); // 총알 지우기
                    return; // thread ends
                }

                if (y < 0) { // 블록 안 맞고 천장에 닿았을 때
                    bulletLabel.setVisible(false); // 총알 지우기
                    return; // thread ends
                }

                bullet.setLocation(x, y);


                try {
                    sleep(45);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    class EnemyThread extends Thread {
        private Enemy enemy;
        private eBulletThread ebulletTh;
        private int leftBound, rightBound; // 이동 가능한 좌표 범위
        private int panelHeight;

        public EnemyThread(Enemy enemy, int panelWidth, int panelHeight) {
            this.enemy = enemy;
            this.panelHeight = panelHeight;
            leftBound = 0;
            rightBound = panelWidth - enemy.getW() ;
        }

        @Override
        public void run() {
            if (ebulletTh == null || !ebulletTh.isAlive()) {
                ebulletTh = new eBulletThread(ebulletLabel, blList, enemy, panelHeight);
                ebulletTh.start();
            }

            while (true) {
                moveLeft();
                moveRight();
            }
        }

        private void moveLeft() {
            for (int i = enemy.getX(); i > leftBound; i -= 5) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    //
                }
                enemy.setLocation(i, enemy.getY());
                enemy.repaint();
            }
        }

        private void moveRight() {
            for (int i = enemy.getX(); i < rightBound; i += 5) {
                enemy.setLocation(i, enemy.getY());
                enemy.repaint();
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    //
                }
            }
        }
    }

    class eBulletThread extends Thread { // 몬스터가 발사하는 총알 스레드
        JLabel ebulletLabel; // 총알 레이블을 저장하는 변수
        Vector<Block> blList; // 블록 리스트
        private Enemy enemy;
        private int panelHeight;
        public eBulletThread(JLabel ebulletLabel, Vector<Block> blList, Enemy enemy, int panelHeight) {
            this.ebulletLabel = ebulletLabel;
            this.blList = blList;
            this.enemy = enemy;
            this.panelHeight = panelHeight;
        }

        public void run() {
            while (true) {
                ebulletLabel.setVisible(true);
                ebulletLabel.setLocation(enemy.getX() + (enemy.getWidth() / 2 - 2), enemy.getY() + 2);

                while (true) {
                    int x = ebulletLabel.getX();
                    int y = ebulletLabel.getY() + 10;
                    int w = ebulletLabel.getWidth();
                    int h = ebulletLabel.getHeight();

                    for (Obs obs : obsList) {
                        if ((((x >= obs.getX()) && (x <= (obs.getX() + obs.getWidth()))) &&
                                (y <= (obs.getY() + obs.getHeight())))) { // 장애물에 닿았을 때
                            ebulletLabel.setLocation(enemy.getX() + (enemy.getWidth() / 2 - 2), enemy.getY() + 2);
                            y = enemy.getY() + 2; // 총알 없애기
                            break; // 여러번 걸쳐서 확인되는 것 수정하기!! //
                        }
                    }

                    if ((((x >= player.getX()) && (x <= (player.getX() + player.getWidth()))) &&
                            (y>=player.getY()))) { // 플레이어에 닿았을 때
                        player.decreaseHealth(10);
                        System.out.println("아야. 플레이어에 닿았어요");
                        ebulletLabel.setLocation(enemy.getX() + (enemy.getWidth() / 2 - 2), enemy.getY() + 2);
                        y = enemy.getY() + 2;
                        break;
                    }

                    if (y > panelHeight) { // 플레이어에 안 맞고 바닥에 닿았을 때
                        x = enemy.getX() + (enemy.getWidth() / 2 - 2);
                        y = enemy.getY() + 2;
                    }

                    ebulletLabel.setLocation(x, y);
                    ebulletLabel.getParent().repaint();

                    try {
                        sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Obs extends JLabel {
        private int x, y, w, h;
        Image img;
        public Obs(int x, int y, int w, int h, ImageIcon icon) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            setBounds(x,y,w,h);
            img = icon.getImage();
        }

        public int getX() { return x; }
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    class ObsThread extends Thread { // 장애물 스레드
        Obs obs;
        private int width;

        public ObsThread(Obs obs,int panelWidth) {
            this.obs = obs;
            this.width = panelWidth;
            obs.setLocation(obs.getX(), obs.getY());
            obs.getParent().repaint();
        }
        @Override
        public void run() {
            while(true) {
                int x = obs.getX() + 5;
                int y = obs.getY();

                if(x > width) {
                    obs.setX(-50);
                    System.out.println("?");
                }
                else
                    obs.setX(x);
                try {
                    sleep(200);
                }
                catch(InterruptedException e) {
                   //
                }
                repaint();
            }
        }
    }
}