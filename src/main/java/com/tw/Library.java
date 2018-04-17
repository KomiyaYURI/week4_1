package com.tw;

//import sun.util.locale.provider.FallbackLocaleProviderAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
* 我们现在做一个应用，该应用是一个命令行应用。当程序启动的时候，我们会看到一个命令行的主界面：
1. 添加学生
2. 生成成绩单
3. 退出
请输入你的选择（1～3）：
如果我们输入1，那么界面就会变成：
请输入学生信息（格式：姓名, 学号, 学科: 成绩, ...），按回车提交：
如果输入格式不正确，就返回：
请按正确的格式输入（格式：姓名, 学号, 学科: 成绩, ...）：
如果输入格式正确就会返回
学生xxx的成绩被添加
然后打印
1. 添加学生
2. 生成成绩单
3. 退出
请输入你的选择（1～3）：
等于回到了主界面。 如果我们在主界面输入了2，那么界面就会变成：
请输入要打印的学生的学号（格式： 学号, 学号,...），按回车提交：
如果我们输入的不正确，就会打印：
请按正确的格式输入要打印的学生的学号（格式： 学号, 学号,...），按回车提交：
如果输入的格式正确，则会打印成绩单并回到主界面。
成绩单
姓名|数学|语文|英语|编程|平均分|总分
========================
张三|75|95|80|80|82.5|330
李四|85|80|70|90|81.25|325
========================
全班总分平均数：xxx
全班总分中位数：xxx
如果我们输入的学号不存在，该学号在计算时就会被忽略。
* */
class Student {
    public int studentNo;
    public String name;
    public double math, chinese, english, program;

    public Student(int studentNo, String name, double math, double chinese, double english, double program)
    {
        this.studentNo = studentNo;
        this.name = name;
        this.math = math;
        this.chinese = chinese;
        this.english = english;
        this.program = program;
    }

    public double getTotalScore()
    {
        return math + chinese + english + program;
    }
}

public class Library {
    int status=0;
    static String[] ss_buf = new String[30];
    static String[] grade_buf = new String[2];

    Scanner sb = new Scanner(System.in);
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    Map<Integer, Student> studentMap = new HashMap<Integer, Student>();

    void readStatus()
    {
        while (true) {
            switch (status)
            {
                case 0: printMenu(); break;
                case 1: getInputData(); break;
                case 2: getInputData(); break;
                case 3: exit(); break;
            }
        }
    }

    void getInputData()
    {
        String str = null;
        switch (status)
        {
            case 1: while (true) {
                        // System.out.println("请输入学生信息（格式：姓名, 学号, 学科: 成绩, ...），按回车提交：");
                        try {
                            str = stdin.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (checkInputValid(str))
                            break;
                    }
                    addStudentInfo(str);
                    break;

            case 2: while (true) {
                        // System.out.println("请输入要打印的学生的学号（格式： 学号, 学号,...），按回车提交：");
                        try {
                            str = stdin.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (checkInputValid(str))
                            break;
                    }
                    printStudentInfo(str);
                    break;
        }
    }

    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    boolean checkInputValid(String str)
    {
        switch (status)
        {
            case 1: boolean mathFlag=false, chineseFlag=false, englishFlag=false, programFlag=false;

                    String[] testCount = str.split(", ");
                    if (testCount.length != 6)
                        return false;

                    // not check name
                    // check student_no
                    if (!isInteger(ss_buf[1]))
                        return false;
                    // check subjects
                    for (int i = 2; i < 6; i++)
                    {
                        grade_buf = testCount[i].split(": ");
                        if (!isDouble(grade_buf[1]))
                            return false;

                        if (grade_buf[0].equals("数学"))
                            mathFlag = true;
                        else if (grade_buf[0].equals("语文"))
                            chineseFlag = true;
                        else if (grade_buf[0].equals("英语"))
                            englishFlag = true;
                        else if (grade_buf[0].equals("编程"))
                            programFlag = true;
                    }

                    if (!(mathFlag && chineseFlag && englishFlag && programFlag))
                        return false;

                    break;
            case 2: ss_buf = str.split(", ");
                    for (String s : ss_buf) {
                        if (!isInteger(s))
                            return false;
                    }
                    break;
        }

        return true;
    }

    void printMenu()
    {
        do {
            // System.out.println("1. 添加学生");
            // System.out.println("2. 生成成绩单");
            // System.out.println("3. 退出");
            // System.out.println("请输入你的选择（1～3）：");

            status = sb.nextInt();
        } while (!(1 <= status && status <= 3));
    }

    void addStudentInfo(String inputStr)
    {
        Student student = null;
        int studentNo;
        String name;
        double math=0, chinese=0, english=0, program=0;

        String str = inputStr;
        str = inputStr;

        ss_buf = str.split(", ");
        name = ss_buf[0];
        studentNo = Integer.parseInt(ss_buf[1]);

        for (int i = 2; i < 6; i++)
        {
            grade_buf = ss_buf[i].split(": ");
            if (grade_buf[0].equals("数学"))
                math = Double.parseDouble(grade_buf[1]);
            else if (grade_buf[0].equals("语文"))
                chinese = Double.parseDouble(grade_buf[1]);
            else if (grade_buf[0].equals("英语"))
                english = Double.parseDouble(grade_buf[1]);
            else if (grade_buf[0].equals("编程"))
                program = Double.parseDouble(grade_buf[1]);
        }

        student = new Student(studentNo, name, math, chinese, english, program);
        studentMap.put(studentNo, student);
        status = 0;
    }
    //
    int printStudentInfo(String inputStr)
    {
        int totalNum=0;
        double totalScore=0;
        Student student = null;

        String str = inputStr;
        List<Double> gradeList = new ArrayList<Double>();

        ss_buf = str.split(", ");
        //  System.out.println("姓名|数学|语文|英语|编程|平均分|总分");
        //  System.out.println("========================");

        for (String s : ss_buf)
        {
            student = studentMap.get(Integer.parseInt(s));
            if (student != null)
            {
                totalNum += 1;

                double tmpTotalScore=0;

                // System.out.print(student.name + "|");
                // System.out.print(student.math + "|");
                // System.out.print(student.chinese + "|");
                // System.out.print(student.english + "|");
                // System.out.print(student.program + "|");

                tmpTotalScore = student.getTotalScore();
                // System.out.print(1.0 * tmpTotalScore / 4 + "|");
                // System.out.print(tmpTotalScore + "\n");

                totalScore += tmpTotalScore;
                gradeList.add(tmpTotalScore);
            }
        }

        Collections.sort(gradeList);

        // System.out.println("========================");
        // System.out.println("全班总分平均数：" + totalScore / totalNum);

        if (gradeList.size() % 2 == 1) {
            // System.out.println("全班总分中位数：" + gradeList.get(gradeList.size() / 2));
            ;
        } else {
            // System.out.println("全班总分中位数：" + (double)(gradeList.get(gradeList.size() / 2) +
            //         gradeList.get(gradeList.size() / 2 - 1)) / 2);
        }

        status = 0;
        return totalNum;
    }

    void exit()
    {
        System.exit(0);
    }

    public boolean someLibraryMethod() {
        return true;
    }

    // System.out.printf("%e",1500.34);

    // Scanner sb = new Scanner(System.in);
    // String name = sb.nextLine();
    // int age = sb.nextInt();


}

