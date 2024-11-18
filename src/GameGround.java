import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

//주 게임화면
public class GameGround extends JPanel {
    private HpPanel hpPanel = null; //클래스 내부 함수 사용을 위한 선언
    private MakeEnemyThread makeEnemyThread = null;
    private PlayerPanel playerPanel = null;
    private GamePanel gamePanel = null;

    //인풋 박스
    private JTextField input = new JTextField(30); //텍스트 입력 박스
    private TextSource textSource = new TextSource();// 단어 벡터 생성


    //배경화면
    private ImageIcon mainBackground = new ImageIcon("mainBackground.png");
    private Image backgroundImage = mainBackground.getImage();
    //벙커 라벨 >> 기능 x
    private ImageIcon bunkerImg = new ImageIcon("bunker.gif");
    //bunkerLabel 4개를 담을 벡터
    private Vector<JLabel> bunkerLabelVector = new Vector<JLabel>();
    private boolean escPressed = false;


    //생성자
    //모든 패널을 MainGamePanel 생성자에 붙인다
    public GameGround(HpPanel hpPanel, PlayerPanel playerPanel, GamePanel gamePanel){  //매개변수로 우측패널 2개 가져오기
        setLayout(null); //배치관리자 사용안함
        this.gamePanel = gamePanel;
        this.playerPanel = playerPanel;
        this.hpPanel = hpPanel;
        add(input);//텍스트 박스 추가
        input.setSize(300, 20);
        input.setLocation(500, 750);
        add(input);
        setSize(1920, 1080);
        setVisible(true);

        this.setFocusable(true);
        this.requestFocus();

        //몬스터 생성 쓰레드 선언

        //---------------------------------------------------------------------------------
        //벙커라벨 벡터 초기화
        for(int i = 0; i < 4; i++){
            bunkerLabelVector.add(new JLabel(bunkerImg));
            bunkerLabelVector.get(i).setSize(bunkerImg.getIconWidth(), bunkerImg.getIconHeight());
            add(bunkerLabelVector.get(i)); //벙커 add
        }

        //벙커 좌표설정
        bunkerLabelVector.get(0).setLocation(0, 40);
        bunkerLabelVector.get(1).setLocation(0, 230);
        bunkerLabelVector.get(2).setLocation(0, 420);
        bunkerLabelVector.get(3).setLocation(0, 610);


        //입력 텍스트필드에 대한 이벤트리스너 >> 엔터 누르면 사용자가 입력한 텍스트 불러와 저장하는 부분
        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField tf = (JTextField)(e.getSource()); //텍스트필드에서 사용자가 입력한 내용 가져오기
                String inputWord = tf.getText();

                if(makeEnemyThread.checkWord(inputWord)){ //입력한 단어가 정답일 때
                    hpPanel.increase();//킬 증가
                    System.out.println("죽였냐");
                    //makeEnemyThread.checkWord(inputWord); // 맞췄나 체크
                }
                tf.setText("");
            }
        });


        // ESC를 누르면 게임 정지 시작
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // keyTyped 메서드를 구현합니다.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (!escPressed) {
                        // 첫 번째 "Esc" 키 눌렀을 때 실행할 코드
                        System.out.println("첫 번째 Esc 키 눌림");
                        makeEnemyThread.allSleep();
                        escPressed = true;
                    }
                    else {
                        // 두 번째 "Esc" 키 눌렀을 때 실행할 코드
                        System.out.println("두 번째 Esc 키 눌림");
                        makeEnemyThread.allWakeUp();
                        escPressed = false;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // keyReleased 메서드를 구현합니다.
            }
        });


        //게임 시스템 스레드 실행
        this.makeEnemyThread = new MakeEnemyThread(this, textSource, hpPanel, playerPanel, gamePanel);
        makeEnemyThread.start();
       
    }


    @Override //컨텐츠팬 그리기
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, 1920, 1080, null); //인트로 배경화면 그리기


    }


    
    //사용할 MakeEnemyThread 제공
    public MakeEnemyThread getMakeEnemyThread(){
        return makeEnemyThread;
    }
}
