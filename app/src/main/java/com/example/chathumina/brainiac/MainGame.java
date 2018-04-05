package com.example.chathumina.brainiac;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainGame extends AppCompatActivity implements View.OnClickListener{

    /*
    * String[] oparators is to hold the math operators that are used to make the questions
    * */
    private static String[] oparators ={"+","-","*","/"};

    /*
    * int[] numbers is to hold the numbers used to generate the questions
    * */
    private static int[] numbers = {1,2,3,4,5,6,7,8,9};

    /*
    * scoreHolder arraylist is to hold the score of each correct question
    * */
    private static ArrayList<Integer> scoreHolder = new ArrayList<>();

    /*
    * Six integers declared bellow will contain the random numbers generated
    * for instance randomOne will hold the first randomly generated number in a question .
    * */
    int randomOne =0, randomTwo =0, randomThree =0, randomeFour =0, randomFive=0, randomSix =0;

    /*
    * Six strings declared bellow will hold math operators in each
    * for instance firstOperator will hold the firth math operator in a question
    * */
    String firstOperator ="", secondOperator ="", thirdOperator="", fourthOperator ="", fifthOperator ="";

    /*
    * five integers to calculate the answer by dividing the question into couples
    * firstCouple will calculate the answer for firstNumber and secondNumber with first math operator
    * */
    int firstCouple =0, secondCouple =0, thirdCouple =0, fourthCouple =0, fifthCouple =0;

    //level is to hold the value relevant to the selected level passed through an Intent
    int level = 0;

    //answer is to hold the answer for each question
    int answer = 0;

    //winCount is to count the correct questions
    int winCount =0;

    //lostCount is to count the wrong questions
    int lostCount =0;

    //count is to count the total number of questions
    int count =0;

    //nextQCount is to check the clicks on Hashtag button
    int nextQCount=0;

    //Timer for the question to answer
    CountDownTimer myTimer;

    //Timer to display Correct or Wrong text for a limited time
    CountDownTimer secondTimer;

    //remainingTime to hold the remaining time when a user click on Hashtag button
    private int remainingTime =0;

    //totalScore to hold the total score of a user
    private int totalScore =0;

    /*private SharedPreferences sp;
    private static final int PREFERENCE_MODE_PRIVATE =0;
    Boolean hintsStatus;*/

    //Alert dialog instance for the GameOver message
    private AlertDialog mDialog;

    boolean switchState;
    private int hintCount =0;
    private int continueGame=0;

    /*
    *Instances to hold TextViews and Buttons declared in the layout xml file
    * */
    private TextView question, wins, loses, answerTxt, timerTxt, messageTxt,saveBtn;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, minusBtn, hastagBtn, clearBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //clearing the scoreHolder arraylist for a new game
        scoreHolder.removeAll(scoreHolder);

        //All the declared buttons and textViews are declared according to the id
        question = (TextView)findViewById(R.id.question);
        wins = (TextView)findViewById(R.id.wins);
        loses = (TextView)findViewById(R.id.loses);
        btn1 = (Button)findViewById(R.id.one);
        btn2 = (Button)findViewById(R.id.two);
        btn3 = (Button)findViewById(R.id.three);
        btn4 = (Button)findViewById(R.id.four);
        btn5 = (Button)findViewById(R.id.five);
        btn6 = (Button)findViewById(R.id.six);
        btn7 = (Button)findViewById(R.id.seven);
        btn8 = (Button)findViewById(R.id.eight);
        btn9 = (Button)findViewById(R.id.nine);
        btn0 = (Button)findViewById(R.id.zero);
        minusBtn = (Button)findViewById(R.id.minus);
        clearBtn = (Button)findViewById(R.id.delete);
        hastagBtn = (Button)findViewById(R.id.hashtag);
        saveBtn = (TextView) findViewById(R.id.hints);
        answerTxt = (TextView)findViewById(R.id.answer);
        timerTxt = (TextView)findViewById(R.id.timer);
        messageTxt = (TextView) findViewById(R.id.messageTxt);

        SharedPreferences prefs = getSharedPreferences("Hints", MODE_PRIVATE);
        switchState = prefs.getBoolean("service_status", false);

        //Action listeners for all the buttons in the layout
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        minusBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        hastagBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        //to make the notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        //function to change the notification bar color to the layout background color
        changeStatusBarColor();


        //mthod to retrieve the value sent through putExtra function using intents in the prevoious activity.
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int passedLevel = extras.getInt("level", -1);
            if(passedLevel>=0){
                level = passedLevel;//level is assigned with the value selected the level selector menu
            }
            int continueBtn = extras.getInt("continue",101);
            if(continueBtn==102){
                continueGame = continueBtn;

            }
        }

        if (continueGame==102){
            SharedPreferences continueG = getSharedPreferences("GameStats", MODE_PRIVATE);
            int newLvel = continueG.getInt("level",0);
            level = newLvel;
            int newAnswer = continueG.getInt("answer",0);
            answer = newAnswer;
            String q = continueG.getString("question","nothing");
            question.setText(q);
            int newCount = continueG.getInt("count",0);
            count = newCount;
            int newWinCount = continueG.getInt("winCount",0);
            winCount = newWinCount;
            wins.setText("Wins:"+winCount);
            int newLostCount = continueG.getInt("lostCount",0);
            lostCount = newLostCount;
            loses.setText("Loses:"+lostCount);
            int newScore = continueG.getInt("score",0);
            totalScore = newScore;
            answerTxt.setText("= ?");
            timer();

            continueGame =0;
            getIntent().removeExtra("continue");
        }else{
            //method to generate questions
            genarateQues();

            //method to start the timer
            timer();
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // to do
        for (Integer score:scoreHolder){
            totalScore = totalScore+score;
        }
        SharedPreferences.Editor editor = getSharedPreferences("GameStats", MODE_PRIVATE).edit();

        //editor.putBoolean("service_status", hints.isChecked());
        editor.putString("question",question.getText().toString());
        editor.putInt("answer",answer);
        editor.putInt("count",count);
        editor.putInt("winCount",winCount);
        editor.putInt("lostCount",lostCount);
        editor.putInt("score",totalScore);
        editor.putInt("level",level);
        boolean b = true;
        editor.putBoolean("gamePlayed",b);
        editor.commit();

    }

    /*
    * All the buttons are assigned with the actions the need to perform in the layout.
    * */
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.hints){//button to turn on the hints option.
            startActivity(new Intent(MainGame.this,Settings.class));
        }
        if(view.getId()==R.id.minus){//minus button
            answerTxt.setText("= -");
        }
        else if(view.getId()==R.id.delete){//delete button to delete letter by letter in anwer text.
            if(answerTxt.getText().toString().endsWith("?")){
                //delete will not affect if nothing is typed in answer text
            }
            else{
                String text = answerTxt.getText().toString();//getting the answer text into a string
                answerTxt.setText(text.substring(0, text.length() - 1));//assigning the answer text again by removing the last element of the string
            }

        }else if(view.getId()==R.id.hashtag){//hasgtag button to change the questions
            if(switchState){
                //Do your work for service is selected on
                hintCount++;
                if(hintCount<4){
                    count++;
                    String answerContent = answerTxt.getText().toString();
                    if(!answerContent.endsWith("?")) {
                        //answer is assigned to an String dismissing the = mark
                        String ans = answerContent.substring(2);
                        if (ans.equals("-")){
                            hintCount=0;
                        }else{
                            int enteredAnswer = Integer.parseInt(ans);
                            if(enteredAnswer>answer){
                                messageTextTimer("LOWER");
                                messageTxt.setTextColor(Color.parseColor("#f9a825"));
                            }
                            if(enteredAnswer<answer){
                                messageTextTimer("HIGHER");
                                messageTxt.setTextColor(Color.parseColor("#6687ff"));
                            }
                            if(enteredAnswer==answer){//if statement to check the user entered answer and the system generated answer is a match
                                //win count is incremented by one
                                winCount++;
                                //win amount is displayed in wins text
                                wins.setText("Wins:"+winCount);
                                //function to display the Correct or Wrong text for two seconds
                                //@params: String
                                messageTextTimer("CORRECT");
                                //setting the color of the Correct text to green color
                                messageTxt.setTextColor(Color.parseColor("#4caf50"));
                                //System.out.println(remainingTime);
                                //calling the score function to calculate the score for the question
                                score();
                                myTimer.cancel();
                                genarateQues();
                                timer();
                            }

                            if(count==10) {//if statement to check if the 10 rounds are over
                                //if so the timer will be stopped
                                myTimer.cancel();
                                //question text will show game over
                                question.setText("Game Over");
                                //answer text assigned to blank
                                answerTxt.setText("");
                                //timer is assigned to zero
                                timerTxt.setText("Timer :0s");
                                score();
                                //for loop to iterate through each index and to get the total score of the user
                                for (Integer score : scoreHolder) {
                                    totalScore = totalScore + score;
                                    //System.out.println(scoreHolder.get(i));
                                }
                                //Alert dialog to display the game over message and the total score to the user.
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainGame.this);
                                builder.setTitle("Game Over");//tittle of the alert box
                                builder.setMessage(" You have finished the 10 rounds.Your score is " + totalScore + " points.");
                                builder.setCancelable(false);
                                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //when user clicks on ok button on alert box user will be displayed again with the main menu.
                                        startActivity(new Intent(MainGame.this, Menu.class));
                                    }
                                });
                                mDialog = builder.show();
                            }
                        }


                    }else{//if user has not entered any answer
                        //lost count get incremented
                        lostCount++;
                        //lost count will be updated in the layout
                        loses.setText("Loses:"+lostCount);
                    }
                }else{
                    //lost count will get incremented by one
                    lostCount++;
                    //lostCount will be displayed to the user
                    loses.setText("Loses:"+lostCount);
                    //function to display the Correct or Wrong text for two seconds
                    //@params: String
                    messageTextTimer("WRONG");
                    //setting the color of the Correct text to red color
                    messageTxt.setTextColor(Color.parseColor("#ff3333"));
                    myTimer.cancel();
                    genarateQues();
                    timer();
                }
            } else {
                //Code for service off
                nextQCount++;
                //getting the user entered answer in answer text to a string
                String answerContent = answerTxt.getText().toString();
                //if statement to check the number of clicks on hashtag button
                if(nextQCount==1){//if user has pressed hashtag only once
                    //count is incremented by one
                    count++;
                    if(!answerContent.endsWith("?")) {//if statement to check whether the user has typed any answer
                        //answer is assigned to an String dismissing the = mark
                        String ans = answerContent.substring(2);
                        if (ans.equals("-")){
                            nextQCount=0;
                        }else{
                            int enteredAnswer = Integer.parseInt(ans);
                            if(enteredAnswer==answer){//if statement to check the user entered answer and the system generated answer is a match
                                //win count is incremented by one
                                winCount++;
                                //win amount is displayed in wins text
                                wins.setText("Wins:"+winCount);
                                //function to display the Correct or Wrong text for two seconds
                                //@params: String
                                messageTextTimer("CORRECT");
                                //setting the color of the Correct text to green color
                                messageTxt.setTextColor(Color.parseColor("#4caf50"));
                                //System.out.println(remainingTime);
                                //calling the score function to calculate the score for the question
                                score();
                            }else{//if user entered answer do not match with the system generated answer
                                //lost count will get incremented by one
                                lostCount++;
                                //lostCount will be displayed to the user
                                loses.setText("Loses:"+lostCount);
                                //function to display the Correct or Wrong text for two seconds
                                //@params: String
                                messageTextTimer("WRONG");
                                //setting the color of the Correct text to red color
                                messageTxt.setTextColor(Color.parseColor("#ff3333"));
                            }

                            myTimer.cancel();
                        }

                    }else{//if user has not entered any answer
                        //lost count get incremented
                        lostCount++;
                        //lost count will be updated in the layout
                        loses.setText("Loses:"+lostCount);
                        myTimer.cancel();
                    }
                    //once the user click the hash tag button the timer will pause
                   // myTimer.cancel();
                }else{//if user press the hashtag button again
                    myTimer.cancel();
                    if(count==10){//if statement to check if the 10 rounds are over
                        //if so the timer will be stopped
                        myTimer.cancel();
                        //question text will show game over
                        question.setText("Game Over");
                        //answer text assigned to blank
                        answerTxt.setText("");
                        //timer is assigned to zero
                        timerTxt.setText("Timer :0s");
                        //for loop to iterate through each index and to get the total score of the user
                        for (Integer score:scoreHolder){
                            totalScore = totalScore+score;
                            //System.out.println(scoreHolder.get(i));
                        }
                        //Alert dialog to display the game over message and the total score to the user.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainGame.this);
                        builder.setTitle("Game Over");//tittle of the alert box
                        builder.setMessage(" You have finished the 10 rounds.Your score is "+totalScore+" points.");
                        builder.setCancelable(false);
                        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                //when user clicks on ok button on alert box user will be displayed again with the main menu.
                                startActivity(new Intent(MainGame.this,Menu.class));
                            }
                        });
                        mDialog = builder.show();
                    }else{//if user has not completed the 10 rounds
                        //a new question is generated on each second click on hashtag button
                        genarateQues();
                        //timer function starts timer again
                        timer();
                    }
                }
            }
        }
        /*
        * Buttons from zero to nine are handled bellow
        * */
        else if(view.getId()==R.id.one){
            int enteredNum = 1;//Button value is assigned
            if(answerTxt.getText().toString().endsWith("?")){//if statement to check whether user has entered any number
                answerTxt.setText("= "+enteredNum);//answer text is updated with the value
            }
            else{//if userhas entered another umber before
                answerTxt.append(""+enteredNum);//this value get appended at the back
            }
        }else if(view.getId()==R.id.two){
            int enteredNum = 2;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.three){
            int enteredNum = 3;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.four){
            int enteredNum = 4;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.five){
            int enteredNum = 5;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.six){
            int enteredNum = 6;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.seven){
            int enteredNum = 7;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.eight){
            int enteredNum = 8;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.nine){
            int enteredNum = 9;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }else if(view.getId()==R.id.zero){
            int enteredNum = 0;
            if(answerTxt.getText().toString().endsWith("?")){
                answerTxt.setText("= "+enteredNum);
            }
            else{
                answerTxt.append(""+enteredNum);
            }
        }
    }

    /*
    * function to change the status bar color in to transparent*/
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /*
    * function to generate questions*/
    private void genarateQues(){
        hintCount=0;
        //number of clicks on hashtag button is set to zero
        nextQCount=0;
        if (level == 0){//if statement to check the user selected level
            randomOne = numbers[randomNumber()];//first randomly generated number in the question
            randomTwo = numbers[randomNumber()];//second randomly generated number in the question
            firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
            String ques = ""+randomOne+firstOperator+randomTwo;//question is assigned to a string
            question.setText(ques);//question text is assigned with the question generated
            answerTxt.setText("= ?");//answer text is assigned with the ? mark
            //answer is calculated using firstSwitch function
            //@params:Integer,Integer
            answer = firstSwitch(randomOne,randomTwo);
        }
        if (level == 1){
            int questionNum = (int) (Math.random()*2)+1;//random number to check the type of the question
            switch (questionNum){//switch case to switch between questions according to the random number generated.
                case 1:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
                    String ques = ""+randomOne+firstOperator+randomTwo;//question is assigned to a string
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using firstSwitch function
                    //@params:Integer,Integer
                    answer = firstSwitch(randomOne,randomTwo);
                    break;
                case 2:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    randomThree = numbers[randomNumber()];//third randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
                    secondOperator = oparators[randomOperator()];//second randomly selected operator in the question
                    ques = ""+randomOne+firstOperator+randomTwo+secondOperator+randomThree;//question is assigned to a string
                    firstSwitch(randomOne,randomTwo);//first two numbers are calculated according to the operator
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using secondSwitch function
                    //@params:Integer
                    answer = secondSwitch(randomThree);
                    break;
            }

        }
        if (level == 2){
            int questionNum = (int) (Math.random()*3)+1;
            switch (questionNum){
                case 1:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    randomThree = numbers[randomNumber()];//third randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
                    secondOperator = oparators[randomOperator()];//second randomly selected operator in the question
                    String ques = ""+randomOne+firstOperator+randomTwo+secondOperator+randomThree;//question is assigned to a string
                    firstSwitch(randomOne,randomTwo);//first two numbers are calculated according to the operator
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using secondSwitch function
                    //@params:Integer
                    answer = secondSwitch(randomThree);
                    break;
                case 2:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
                    ques = ""+randomOne+firstOperator+randomTwo;//question is assigned to a string
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using firstSwitch function
                    //@params:Integer,Integer
                    answer = firstSwitch(randomOne,randomTwo);
                    break;
                case 3:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    randomThree = numbers[randomNumber()];//third randomly generated number in the question
                    randomeFour = numbers[randomNumber()];//fourth randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly generated operator in the question
                    secondOperator = oparators[randomOperator()];//second randomly generated operator in the question
                    thirdOperator = oparators[randomOperator()];//third randomly generated operator in the question
                    //question is assigned to a string
                    ques = ""+randomOne+firstOperator+randomTwo+secondOperator+randomThree+thirdOperator+randomeFour;
                    firstSwitch(randomOne,randomTwo);//first two numbers are calculated according to the operator
                    secondSwitch(randomThree);//third number and the calculated amount from firstSwitch are calculated
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using thirdSwitch function
                    //@params:Integer
                    answer = thirdSwitch(randomeFour);
                    break;
            }

        }
        if (level == 3){
            int questionNum = (int) (Math.random()*3)+1;
            switch (questionNum){
                case 1:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    randomThree = numbers[randomNumber()];//third randomly generated number in the question
                    randomeFour = numbers[randomNumber()];//fourth randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly generated operator in the question
                    secondOperator = oparators[randomOperator()];//second randomly generated operator in the question
                    thirdOperator = oparators[randomOperator()];//third randomly generated operator in the question
                    //question is assigned to a string
                    String ques = ""+randomOne+firstOperator+randomTwo+secondOperator+randomThree+thirdOperator+randomeFour;
                    firstSwitch(randomOne,randomTwo);//first two numbers are calculated according to the operator
                    secondSwitch(randomThree);//third number and the calculated amount from firstSwitch are calculated
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using thirdSwitch function
                    //@params:Integer
                    answer = thirdSwitch(randomeFour);
                    break;
                case 2:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    randomThree = numbers[randomNumber()];//third randomly generated number in the question
                    randomeFour = numbers[randomNumber()];//fourth randomly generated number in the question
                    randomFive = numbers[randomNumber()];//fifth randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
                    secondOperator = oparators[randomOperator()];//second randomly selected operator in the question
                    thirdOperator = oparators[randomOperator()];//third randomly selected operator in the question
                    fourthOperator = oparators[randomOperator()];//fourth randomly selected operator in the question
                    //question is assigned to a string
                    ques = ""+randomOne+firstOperator+randomTwo+secondOperator+randomThree+thirdOperator+randomeFour+fourthOperator+
                            randomFive;
                    firstSwitch(randomOne,randomTwo);//first two numbers are calculated according to the operator
                    secondSwitch(randomThree);//third number and the calculated amount from firstSwitch are calculated
                    thirdSwitch(randomeFour);//fourth number and the calculated amount from secondSwitch are calculated
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using thirdSwitch function
                    //@params:Integer
                    answer = fourthSwitch(randomFive);
                    break;
                case 3:
                    randomOne = numbers[randomNumber()];//first randomly generated number in the question
                    randomTwo = numbers[randomNumber()];//second randomly generated number in the question
                    randomThree = numbers[randomNumber()];//third randomly generated number in the question
                    randomeFour = numbers[randomNumber()];//fourth randomly generated number in the question
                    randomFive = numbers[randomNumber()];//fifth randomly generated number in the question
                    randomSix = numbers[randomNumber()];//sixth randomly generated number in the question
                    firstOperator = oparators[randomOperator()];//first randomly selected operator in the question
                    secondOperator = oparators[randomOperator()];//second randomly selected operator in the question
                    thirdOperator = oparators[randomOperator()];//third randomly selected operator in the question
                    fourthOperator = oparators[randomOperator()];//fourth randomly selected operator in the question
                    fifthOperator = oparators[randomOperator()];//fifth randomly selected operator in the question
                    //question is assigned to a string
                    ques = ""+randomOne+firstOperator+randomTwo+secondOperator+randomThree+thirdOperator+randomeFour+
                            fourthOperator+randomFive+fifthOperator+randomSix;
                    firstSwitch(randomOne,randomTwo);//first two numbers are calculated according to the operator
                    secondSwitch(randomThree);//third number and the calculated amount from firstSwitch are calculated
                    thirdSwitch(randomeFour);//fourth number and the calculated amount from secondSwitch are calculated
                    fourthSwitch(randomFive);//fifth number and the calculated amount from thirdSwitch are calculated
                    question.setText(ques);//question text is assigned with the question generated
                    answerTxt.setText("= ?");//answer text is assigned with the ? mark
                    //answer is calculated using fifthSwitch function
                    //@params:Integer
                    answer = fifthSwitch(randomSix);
                    break;
            }

        }
    }

    /*
    * method to calculate the first two numbers in the question*/
    private int firstSwitch(int a, int b){
        switch (firstOperator){
            case "+":
                firstCouple = a+b;
                return firstCouple;
            case "-":
                firstCouple = a-b;
                return firstCouple;
            case "*":
                firstCouple = a*b;
                return firstCouple;
            case "/":
                firstCouple = a/b;
                return firstCouple;
        }
        return 0;
    }

    /*
    * method to calculate the third number and the answer from firstSwitch method*/
    private int secondSwitch(int a){
        switch (secondOperator){
            case "+":
                secondCouple = firstCouple+a;
                return secondCouple;
            case "-":
                secondCouple = firstCouple-a;
                return secondCouple;
            case "*":
                secondCouple = firstCouple*a;
                return secondCouple;
            case "/":
                secondCouple = firstCouple/a;
                return secondCouple;
        }
        return 0;
    }

    /*
    * method to calculate the fourth number and the answer from secondSwitch method*/
    private int thirdSwitch(int a){
        switch (thirdOperator){
            case "+":
                thirdCouple = secondCouple+a;
                return thirdCouple;
            case "-":
                thirdCouple = secondCouple-a;
                return thirdCouple;
            case "*":
                thirdCouple = secondCouple*a;
                return thirdCouple;
            case "/":
                thirdCouple = secondCouple/a;
                return thirdCouple;
        }
        return 0;
    }

    /*
    * method to calculate the fifth number and the answer from thirdSwitch method*/
    private int fourthSwitch(int a){
        switch (fourthOperator){
            case "+":
                fourthCouple = thirdCouple+a;
                return fourthCouple;
            case "-":
                fourthCouple = thirdCouple-a;
                return fourthCouple;
            case "*":
                fourthCouple = thirdCouple*a;
                return fourthCouple;
            case "/":
                fourthCouple = thirdCouple/a;
                return fourthCouple;
        }
        return 0;
    }

    /*
    * method to calculate the sixth number and the answer from fourthSwitch method*/
    private int fifthSwitch(int a){
        switch (fifthOperator){
            case "+":
                fifthCouple = fourthCouple+a;
                return fifthCouple;
            case "-":
                fifthCouple = fourthCouple-a;
                return fifthCouple;
            case "*":
                fifthCouple = fourthCouple*a;
                return fifthCouple;
            case "/":
                fifthCouple = fourthCouple/a;
                return fifthCouple;
        }
        return 0;
    }

    /*
    * method to generate a random number between 0 and 8*/
    private  static int randomNumber(){
        return (int) (Math.random()*9);
    }

    /*
    * method to generate a random number between 0 and 3*/
    private  static int randomOperator(){
        return (int) (Math.random()*4);
    }


    /*
    * method to generate the count down timer for 10 seconds*/
    private void timer(){
        myTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTxt.setText("Timer: " + millisUntilFinished / 1000+"s");//Timer text is updated with the tick of a second
                remainingTime = (int) (millisUntilFinished/1000);//remaining time is assigned with the current time in the tick
            }

            public void onFinish() {//contains what to execute after finishing timer
                //lost count is incremented by one
                lostCount++;
                loses.setText("Loses:"+lostCount);//Loses text updated with the lost amount
                count++;//total question count is incremented by one
                genarateQues();//generate questions method called to generate new question
                timer();//timer function is called to start the timer again
            }
        }.start();
    }

    /*
    * method to display Win or Lose text for two seconds
    * @param: String*/
    private void messageTextTimer(final String message){
        secondTimer = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                messageTxt.setVisibility(View.VISIBLE);//message text is set visible for two seconds
                messageTxt.setText(message);//message text is updated with the parameter passed
            }

            public void onFinish() {//contains what to execute after finishing timer
                messageTxt.setVisibility(View.INVISIBLE);//message text is set to invisible
                secondTimer.cancel();
            }
        }.start();
    }

    /*
    * method to calculate the score for each question*/
    private void score(){
        int timeRemaining = remainingTime;//remaining time is assigned
        if(timeRemaining<20){//checks for the remaining time amount
            int scoreperQuestion = 100/(20-timeRemaining);//logic for the score calculation
            scoreHolder.add(scoreperQuestion);//arraylist is populated with the score for the specific question
        }else{//if remaining time is 10 seconds
            //score is 100
            int score = 100;
            scoreHolder.add(score);//arraylist is populated with the score for the specific question
        }
        for (Integer score:scoreHolder){
            totalScore = totalScore+score;
        }
        saveBtn.setText(""+totalScore);
    }
}
