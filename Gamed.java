package rockv3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Gamed {
    static int cumwin = 0; //컴퓨터 전적
    static int userwin = 0; //유저 전적
    static int draw = 0; //무승부 횟수
    static Scanner sc = new Scanner(System.in);
    static String filePath = ""; // 파일 경로를 전역 변수로 선언

    public static void main(String[] args) {
        filePath = "#filedirectorydes.txt"; // 파일 경로 생성 , 추후 로그인 이나 회원가입 클래스가 완성되면
        //로그인한 유저값에 맞게 생성하거나, 그 정보에 전적을 누적할 예정)

        int[] values = readValuesFromFile(filePath); // 파일에서 전적 값 읽어오기

        if (values != null) {
            System.out.println("전적: 컴퓨터 전적 " + values[0] + "승 / 유저 전적 " + values[1] + "승 / 무승부 횟수 " + values[2]);
            cumwin = values[0]; //컴퓨터 전적 값 설정
            userwin = values[1]; //유저 전적 값 설정
            draw = values[2]; //무승부 전적 값    설정
        } else {
            int[] defaultValues = {0, 0, 0}; //기본 전적 값
            createFileWithDefaultValues(filePath, defaultValues);
        }

        gameIng();
    }

    private static int[] readValuesFromFile(String filePath) { //파일에서 전적 값 읽어들이는 메서드
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int[] values = new int[3];

            for (int i = 0; i < 3; i++) {
                String line = bufferedReader.readLine();
                values[i] = Integer.parseInt(line);
            }

            bufferedReader.close();

            return values;
        } catch (IOException | NumberFormatException e) {
            System.out.println("사용자 파일이 존재하지 않거나 손상되었습니다.");
            return null;
        }
    }

    //파일 생성 하고 기본 전적값을 저장 하는 메서드
    private static void createFileWithDefaultValues(String filePath, int[] defaultValues) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);

            for (int i = 0; i < 3; i++) {
                fileWriter.write(defaultValues[i] + "\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //게임 시작
    public static void startGame() {
        System.out.println("가위(1) / 바위(2) / 보(3) 중 하나를 입력하세요: ");
        String[] input = {"가위", "바위", "보"};
        String str = sc.next(); //선택
        int userNum = 0;
        if (str.equals("가위") || str.equals("1")) {
            userNum = 1;
        } else if (str.equals("바위") || str.equals("2")) {
            userNum = 2;
        } else if (str.equals("보") || str.equals("3")) {
            userNum = 3;
        }

        Random random = new Random();
        int cumNum = random.nextInt(3) + 1;

        if (cumNum > userNum) {
            if (cumNum == 3 && userNum == 1) {
                userwin++;
                System.out.println("Computer: " + input[cumNum - 1] + "\nUser: " + input[userNum - 1] + "\n결과: User 승");
            } else {
                cumwin++;
                System.out.println("Computer: " + input[cumNum - 1] + "\nUser: " + input[userNum - 1] + "\n결과: Computer 승");
            }
        } else if (cumNum < userNum) {
            if (cumNum == 1 && userNum == 3) {
                cumwin++;
                System.out.println("Computer: " + input[cumNum - 1] + "\nUser: " + input[userNum - 1] + "\n결과: Computer 승");
            } else {
                userwin++;
                System.out.println("Computer: " + input[cumNum - 1] + "\nUser: " + input[userNum - 1] + "\n결과: User 승");
            }
        } else {
            draw++;
            System.out.println("Computer: " + input[cumNum - 1] + "\nUser: " + input[userNum - 1] + "\n결과: 무승부");
        }

        int[] values = {cumwin, userwin, draw};
        writeValuesToFile(filePath, values);
        gameIng();
    }


    //전적값을 파일에 쓰기.
    private static void writeValuesToFile(String filePath, int[] values) {
        //예외가 발생할 경우 예외 처리 및 출력
        try {
            FileWriter fileWriter = new FileWriter(filePath);//파일 열고

            for (int i = 0; i < 3; i++) {
                fileWriter.write(values[i] + "\n");//각 값을 파일에 넣기
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //게임 끝나고, 이후
    private static void gameIng() {
        while (true) {
            System.out.println("1. 게임 계속하기   2. 전적 보기   3. 종료");
            int a = sc.nextInt();
            switch (a) {
                case 1:
                    startGame(); //게임 계속 하기 하면, startGame()메서드 호출
                    break;
                case 2:
                    gameRate(); // 전적 보개면, gameRate() 메서드 호출
                    break;
                case 3:
                    System.out.println("종료");
                    System.exit(0);
            }
        }
    }

    static void gameRate() { //전적 출력하기 역할
        LocalDateTime now = LocalDateTime.now(); //현재 날짜와 시간 얻기.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"); //원하는 방식으로 초기화하기.
        String formattedDateTime = now.format(formatter);

        double result = ((double) userwin / (double) (cumwin + userwin + draw)) * 100;
        System.out.println(
                formattedDateTime + " 전적: 승(" + userwin + ") 패(" + cumwin + ") 무승부: " + draw + " 승률(" + (int) result + "%)");
    }
}
