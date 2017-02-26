package com.sanri.game.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
/**
 *
 * @author 晓之篷
 */
@SuppressWarnings("serial")
public class Sudoku extends JFrame{
    Object[] fivelevel={1,2,3,4,5};
    Object level=JOptionPane.showInputDialog(null, "1.初级--2.低级--3.中级--4高级--5.骨灰级", "请选择难度", JOptionPane.QUESTION_MESSAGE, null,fivelevel, fivelevel[2]);
    private IndexButton[][] button=new IndexButton[9][9];
    private JButton check=new JButton("检查");  //检查正确是否
    private JButton displayanswer=new JButton("显示答案");  //列出答案
    private JButton hideanswer=new JButton("隐藏答案");  //隐藏答案
    private CSSudoku cssudoku= null;//new CSSudoku((int)(level));   //创建和解答数独
    private int[][] shudu=cssudoku.MySudoku();   //把创建的数独赋值给shudu[][]
    public Sudoku() {
        JPanel pbutton=new JPanel();
        pbutton.setLayout(new GridLayout(1,1,5,5));
        pbutton.add(check);
        pbutton.add(displayanswer);
        pbutton.add(hideanswer);
        hideanswer.setEnabled(false);
        add(new SudokuPanel(shudu),BorderLayout.CENTER);
        add(pbutton,BorderLayout.SOUTH);
         for(int i=0;i<9;i++){       //给每个按钮增加监听事件
                for(int j=0;j<9;j++){
                    button[i][j].addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        IndexButton b=(IndexButton) e.getSource();
                        UpdateButton(b.geti(),b.getj());
                    }
                });  
                }
         }
        check.addActionListener(new ActionListener() {   //给check增加监听事件
            @Override
            public void actionPerformed(ActionEvent e) {
                if(CSSudoku.test(shudu)==1){  //检查结果正确
                    JOptionPane.showMessageDialog(null, "正确", "检查结果", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "有错误", "检查结果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        displayanswer.addActionListener(new ActionListener() {   //显示答案的监听事件
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] result=new int[9][9];
                for(int i=0;i<9;i++){
                    for(int j=0;j<9;j++) {
                        result[i][j]=shudu[i][j];
                    }         
                }
                if(cssudoku.SolveSudoku(result)){   //得到答案
                     result=cssudoku.ResultSudoku();
                     for(int i=0;i<9;i++){         
                        for(int j=0;j<9;j++){
                            if(button[i][j].getBackground()==Color.WHITE||button[i][j].getBackground()==Color.RED){     //将玩家已经输入值的按钮设置成不可操作
                               button[i][j].setEnabled(false); 
                            }
                            if(shudu[i][j]==0){
                                button[i][j].setText(String.valueOf(result[i][j]));
                                button[i][j].setBackground(Color.ORANGE);
                                button[i][j].setEnabled(false);
                                displayanswer.setEnabled(false);
                                hideanswer.setEnabled(true);
                            }     
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"无解");
                }
            }
        });
        hideanswer.addActionListener(new ActionListener() {   //隐藏答案的监听事件
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<9;i++){         
                        for(int j=0;j<9;j++){
                            if(button[i][j].getBackground()==Color.WHITE||button[i][j].getBackground()==Color.RED){     //将玩家已经输入值的按钮设置成可操作
                               button[i][j].setEnabled(true); 
                            }
                            if(button[i][j].getBackground()==Color.ORANGE){  //将给出的答案隐藏
                                button[i][j].setText("");
                                button[i][j].setBackground(Color.WHITE);
                                button[i][j].setEnabled(true);
                                displayanswer.setEnabled(true);
                                hideanswer.setEnabled(false);
                            }     
                        }
                    }
            }
        });
    }
    public void UpdateButton(int m,int n){   //当点下按钮button[m][n]对其进行的更新操作
        IndexButton b=button[m][n];
        b.setBackground(Color.red);
        for(int i=0;i<9;i++){         //始终保持只有一个RED
            for(int j=0;j<9;j++){
                if(button[i][j].getBackground()==Color.RED&&button[i][j]!=b){
                    button[i][j].setBackground(Color.WHITE);
                }
            }
        }
        b.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                IndexButton b=(IndexButton) e.getSource();
                if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){  //按下BackSpace,消除数字
                    shudu[b.geti()][b.getj()]=0;
                    b.setText("");
                    b.setBackground(Color.WHITE);
                }
                for(int i=49;i<58;i++){   // 数字1-9的ASCII码为49-57
                    if(e.getKeyChar()==i){
                        shudu[b.geti()][b.getj()]=i-48;
                        b.setText(String.valueOf(i-48));
                        b.setBackground(Color.WHITE);
                    }
                }
            }

        });
    }
    
    public static void main(String[] args) {
        
        Sudoku frame=new Sudoku();
        frame.setTitle("数独");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public class SudokuPanel extends JPanel{    //数独面板
        public SudokuPanel(){
            
        }
        public SudokuPanel(int[][] a) {   //创建一个数独为a的面板
            setLayout(new GridLayout(3,3));
            JPanel[] p=new JPanel[9];  //9个小面板
            for(int i=0;i<9;i++){    //加入9个面板
                p[i]=new JPanel();
                p[i].setLayout(new GridLayout(3,3,1,1));
                p[i].setBorder(new LineBorder(Color.BLACK,2));
                add(p[i]);
            }
            for(int i=0;i<9;i++){     // 加入全部的按键
                for(int j=0;j<9;j++){
                    int m=3*(i/3)+j/3;  //m表示按钮button[i][j]在m号面板
                    button[i][j]=new IndexButton("",i,j);
                    InitButton(i,j,a);
                    p[m].add(button[i][j]);  //把按钮放入小面板
                }
            }
        }
        public void InitButton(int i,int j,int[][] a){  //初始化按钮button[i][j]
            button[i][j].setBackground(Color.WHITE);
            if(shudu[i][j]!=0){
                button[i][j].setText(String.valueOf(a[i][j]));
                button[i][j].setBackground(Color.darkGray);
                button[i][j].setEnabled(false);
            }   
            button[i][j].setFont(new Font("SansSerif",Font.BOLD,30));
        }
    }
}
@SuppressWarnings("serial")
class IndexButton extends JButton{   //继承JButton，可以获取数组按钮的角标
    private int i,j;
    public IndexButton(String text,int i,int j){
        super(text);
        this.i=i;
        this.j=j;
    }
    public int geti(){
        return i;
    }
     public int getj(){
        return j;
    }
}
class CSSudoku {
    private int[][] a={   //初始化生成数独
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}
             };
    private int[][] result;   //储存答案的数独
    private int MINFILLED;   //对应难度下 最小的已知格子数
    private int MINKNOW;     //对应难度下 行列已知格的底线
    public CSSudoku() {   
        LevelSudoku(3);
    }
    public CSSudoku(int n) { 
        if(n>0&&n<6){
            LevelSudoku(n);
        }else{
            LevelSudoku(3);
        }
    }
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public int[][] MySudoku(){   //返回生成的数独
        return a;
    }
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public int[][] ResultSudoku(){   //返回数独答案
        return result;
    }
    public void RandSudoku(int n){   //随机选取n个空格放入1-9的随机数
        Random rand=new Random();
        int rand1=0;
        Point[] rand2=new Point[n];  //rand2储存n个空格的行列
        for(int k=0;k<n;k++){  //初始化
            rand2[k]=new Point();
        }
        for(int k=0;k<n;k++){         //在n个空格产生随机数
            rand2[k].x=rand.nextInt(9);  //产生0-8的随机数,表示第k个空格的行
            rand2[k].y=rand.nextInt(9);  //产生0-8的随机数,表示第k个空格的列
            for(int p=0;p<k;p++){
                if(rand2[k].equals(rand2[p])){  //生成了重复位置的空格
                    k--;                //重新生成
                    break;
                }
            }
            
        }
        for(int k=0;k<n;k++){         
            rand1=1+rand.nextInt(9);
            a[rand2[k].x][rand2[k].y]=rand1;
            if(test(a,rand2[k].x,rand2[k].y)==0){  // 生成的空格填入的数字错误
                k--;
            }
        }
        
    }
    public void LevelSudoku(int n){   //生成一个指定等级的数独，且只有唯一解
        Random rand=new Random();
        int p=rand.nextInt(9),q=rand.nextInt(9);    //初始当前挖洞位置
        int P=p,Q=q;  //保存初始挖洞位置
        Point next=new Point();  //下一个挖洞位置
        int filled=81;  //当前已知格子数
        int minknow=9;  //当前行列最小的已知格子数   
        RandSudoku(11);   //随机产生11个已知格的数独
        if(SolveSudoku(a)){   //生成的数独有解（99.7%有解）
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++) {
                    a[i][j]=result[i][j]; //将终盘赋值给a[][]
                }         
            }
        }
        else{
            System.exit(0);
        }
        a=EqualChange(a);     //等效变换
        SetLevel(n);   //初始化MINFILLED和MINKNOW
        do{
            if(isOnlyOne(p,q,a)&&MinKnow(p,q,a)>=MINKNOW){    //此洞可挖
                a[p][q]=0;
                filled--;
                minknow=MinKnow(p,q,a);
            }
            next=FindNext(p,q,n);
            p=next.x;
            q=next.y;
            if(n==1||n==2){
                while(p==P&&q==Q){
                     next=FindNext(p,q,n);
                     p=next.x;
                     q=next.y;
                }
            }
        }while(filled>MINFILLED&&(P!=p||Q!=q));
    }
    public int[][] EqualChange(int[][] b){   //将终盘等效变换
        Random rand=new Random();
        int num1=1+rand.nextInt(9);   //将所有的1与num1互换
        int num2=1+rand.nextInt(9);   //将所有的2与num2互换
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(b[i][j]==1){
                    b[i][j]=num1;
                }
                else if(b[i][j]==num1){
                    b[i][j]=1;
                }
                
                if(b[i][j]==2){
                    b[i][j]=num2;
                }
                else if(b[i][j]==num2){
                    b[i][j]=2;
                }
            }
        }
        return b;
    }
    public void SetLevel(int n){   //设置难度等级 ,1-5对应初级，低级，中级，高级，骨灰级
        Random rand=new Random();
        switch(n){     //初始化MINFILLED和MINKNOW
            case 1: MINFILLED=50+rand.nextInt(5); MINKNOW=5; break;
            case 2: MINFILLED=45+rand.nextInt(5); MINKNOW=4; break;
            case 3: MINFILLED=31+rand.nextInt(10); MINKNOW=3; break;
            case 4: MINFILLED=21+rand.nextInt(10); MINKNOW=2; break;
            case 5: MINFILLED=17+rand.nextInt(10); MINKNOW=0; break;    
        }
    }
    
    public Point FindNext(int i,int j,int n){   //设置对应难度下的挖洞顺序，参数i,j表示当前要挖洞的位置，n表示难度，返回值是下一个要挖的洞的位置
        Random rand=new Random(); 
        Point next=new Point();
        switch(n){
            case 1:                                                             //难度1随机
            case 2: next.x=rand.nextInt(9); next.y=rand.nextInt(9);break;   //难度2随机
            case 3:                                                          //难度3间隔
                if(i==8&&j==7){
                    next.x=0;
                    next.y=0;
                }
                else if(i==8&&j==8){
                    next.x=0;
                    next.y=1;
                }
                else if((i%2==0&&j==7)||(i%2==1)&&j==0){
                    next.x=i+1;
                    next.y=j+1;
                }
                else if((i%2==0&&j==8)||(i%2==1)&&j==1){
                    next.x=i+1;
                    next.y=j-1;
                }
                else if(i%2==0){
                    next.x=i;
                    next.y=j+2;
                }
                else if(i%2==1){
                    next.x=i;
                    next.y=j-2;
                }break;
            case 4:                                                             //难度4蛇形
                if(i==8&&j==8){
                    next.y=0;
                }
                else if(i%2==0&&j<8){                                                //蛇形顺序，对下个位置列的求解
                    next.y=j+1;
                }
                else if((i%2==0&&j==8)||(i%2==1&&j==0)){
                    next.y=j;
                }
                else if(i%2==1&&j>0){
                    next.y=j-1;
                }
                
                if(i==8&&j==8){                                                   //蛇形顺序，对下个位置行的求解
                    next.x=0;
                }
                else if((i%2==0&&j==8)||(i%2==1)&&j==0){
                    next.x=i+1;
                }
                else{
                    next.x=i;
                }break;
            case 5:                                                             //难度5从左至右，自顶向下
                if(j==8){
                    if(i==8){
                        next.x=0;
                    }
                    else {
                        next.x=i+1;
                    }
                    next.y=0;
                }
                else{
                    next.x=i;
                    next.y=j+1;
                } break;
        }
        return next;
    }
    public boolean isOnlyOne(int i,int j,int[][] b){  //判断在i,j挖去数字后是否有唯一解
        int k=b[i][j];  //待挖洞的原始数字
        for(int num=1;num<10;num++){
            b[i][j]=num;
            if(num!=k&&SolveSudoku(b)){     //除待挖的数字之外，还有其他的解，则返回false
                b[i][j]=k;
                return false;
            }
        }
        b[i][j]=k;
        return true;     //只有唯一解则返回true
    }
    public static int MinKnow(int p,int q,int[][] b){  //返回若将p q挖去后行列中已知格数的低限
        int temp=b[p][q];
        int minknow=9;
        int tempknow=9;
        b[p][q]=0;
        for(int i=0;i<9;i++){    //搜索行最小已知
            for(int j=0;j<9;j++){
                if(b[i][j]==0){
                    tempknow--;
                    if(tempknow<minknow){
                        minknow=tempknow;
                    }
                }
            }
            tempknow=9;
        }
        tempknow=9;
        for(int j=0;j<9;j++){    //搜索列最小已知
            for(int i=0;i<9;i++){
                 if(b[i][j]==0){
                    tempknow--;
                    if(tempknow<minknow){
                        minknow=tempknow;
                    }
                 }
            }
            tempknow=9;
        }
        b[p][q]=temp;
        return minknow;
    }
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public boolean SolveSudoku(int[][] b){  //判断数独b是否能解
        int cout=0;
        int[][] temp =new int[9][9];   //复制数独b
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++){
                temp[i][j]=b[i][j];
                if(temp[i][j]==0){
                    cout++;
                }
            }
        }
        Point[] fill=new Point[cout];  //储存空位子的横纵坐标
        int k=0;
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++){
                if(temp[i][j]==0){
                    fill[k]=new Point(i,j);
                    k++;       //空位子数目
                }
            }
        }
        if(test(temp)==0){
            return false;
        }
        else if(k==0&&test(temp)==1){   //玩家填满格子，并且正确，再来求答案
            JOptionPane.showMessageDialog(null,"答案正确");
            return true;
        }
        else if(put(temp,0,fill)){ 
            result=output(temp);
            return true;
        }
        else{
            return false;
        }
    }
   
    public static boolean put(int b[][],int n,Point fill[]){  //在第n个空位子放入数字
        if (n < fill.length) {
            for (int i = 1; i < 10; i++) {
                b[fill[n].x][fill[n].y] = i;
                if (test(b,fill[n].x,fill[n].y)==1 &&put(b, n+1, fill)) {
                    return true;
                }
            }
            b[fill[n].x][fill[n].y] = 0; 
            return false;
        }
        else {
            return true;
        }
    }
    public static int[][] output(int[][] b) {   //输出结果
        return b;
    }
    /**
     * m,i 代表行
     * n,j 代表列
     * 返回 0 代表出错
     */
    public static int test(int[][] b,int i,int j){    //检验位置i,j 数字num是否可行
        int m=0,n=0,p=0,q=0;   //m,n是计数器，p,q用于确定test点的方格位置
        for(m=0;m<9;m++) {
            if(m!=i&&b[m][j]==b[i][j]){
                return 0;
            }
        }
        for(n=0;n<9;n++) {
            if(n!=j&&b[i][n]==b[i][j]){
                return 0;
            }
        }
        for(p=i/3*3,m=0;m<3;m++) {
            for(q=j/3*3,n=0;n<3;n++) {
                if((p+m!=i||q+n!=j)&&(b[p+m][q+n]==b[i][j])){
                    return 0;
                }
            }
        }
        return 1;       
    }
    public static int test(int[][] b) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (b[i][j] != 0 && CSSudoku.test(b, i, j)==0) {
                    return 0;
                }
            }
        }
        return 1; 
   }
}