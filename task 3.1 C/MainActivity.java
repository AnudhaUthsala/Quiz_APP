package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static QuizAdapter quizAdapter;
    private ProgressBar progressBar;
    private TextView prograss;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Global.answersModelList.clear();
        progressBar  = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.quiz);
        next         = findViewById(R.id.next);
        prograss     = findViewById(R.id.progress);

        Global.quizList.add(new QuizModel(
                "What is the capital city of France?",
                Arrays.asList("Paris", "London", "Berlin", "Rome"),
                1
        ));

        // History
        Global.quizList.add(new QuizModel(
                "Who was the first President of the United States?",
                Arrays.asList("George Washington", "Thomas Jefferson", "Abraham Lincoln", "John Adams"),
                1
        ));

        // Science
        Global.quizList.add(new QuizModel(
                "What is the chemical symbol for water?",
                Arrays.asList("H", "O", "H2", "H2O"),
                4
        ));

        // Literature
        Global.quizList.add(new QuizModel(
                "Who wrote the play 'Romeo and Juliet'?",
                Arrays.asList("William Shakespeare", "Charles Dickens", "Jane Austen", "Mark Twain"),
                1
        ));

        // Movies
        Global.quizList.add(new QuizModel(
                "What film won the Academy Award for Best Picture in 1994?",
                Arrays.asList("Forrest Gump", "Pulp Fiction", "The Shawshank Redemption", "Schindler's List"),
                1
        ));

        // Sports
        Global.quizList.add(new QuizModel(
                "In which sport would you perform a slam dunk?",
                Arrays.asList("Basketball", "Football", "Tennis", "Golf"),
                1
        ));

        // Music
        Global.quizList.add(new QuizModel(
                "Who was the lead vocalist of the band Queen?",
                Arrays.asList("Freddie Mercury", "John Lennon", "Elton John", "Michael Jackson"),
                1
        ));

        // Technology
        Global.quizList.add(new QuizModel(
                "Who is the CEO of Tesla Inc.?",
                Arrays.asList("Elon Musk", "Bill Gates", "Jeff Bezos", "Mark Zuckerberg"),
                1
        ));

        // Math
        Global.quizList.add(new QuizModel(
                "What is the square root of 64?",
                Arrays.asList("4", "6", "7", "8"),
                4
        ));

        // General Knowledge
        Global.quizList.add(new QuizModel(
                "What is the tallest mountain in the world?",
                Arrays.asList("Mount Everest", "K2", "Kangchenjunga", "Lhotse"),
                1
        ));


        // Start long running operation in a background thread
        new Thread(() -> {
            while (progressStatus < 100) {
                int a = Global.answersModelList.size()*100;
                int b = Global.quizList.size();
                progressStatus = a/b;

                // Update the progress bar and display the current value in percentage
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    // Display the current progress in percentage
                    // You may adjust this formatting according to your needs
                    setTitle("Complete " + progressStatus + "%");
                    prograss.setText(String.valueOf(a/100) +"/ " + String.valueOf(b));
                });

                try {
                    // Sleep for 50 milliseconds to simulate a long operation
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.answersModelList.size() == Global.quizList.size()){
                    Intent intent = new Intent(MainActivity.this, FinishQuiz.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Answer All Questions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        quizAdapter = new QuizAdapter(this,Global.quizList);
        recyclerView.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();

    }
}


//AnserModel.java

package com.example.quizapp;

public class AnswersModel {
    int question;
    int answer;

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}


//FinishQuiz.java

package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishQuiz extends AppCompatActivity {
    private TextView textView3, textView4, textView5;
    private Button buttonNewQuiz, buttonFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quiz);

        // Initialize the TextViews and Buttons
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        buttonNewQuiz = findViewById(R.id.button2);
        buttonFinished = findViewById(R.id.button3);

        int correctAnswers = 0;
        String name = Global.Name;
        textView3.setText("Congratulations " + name);
        for(int i =0 ; i< Global.answersModelList.size(); i++){
            for(int j =0 ; j< Global.quizList.size(); j++){
                if((Global.answersModelList.get(i).getQuestion() == j+1) && (Global.answersModelList.get(i).getAnswer() == Global.quizList.get(j).getAnswer())){
                    correctAnswers = correctAnswers + 1;
                }
            }

        }
        String correctAnswer = String.valueOf(correctAnswers);
        String all           = String.valueOf(Global.quizList.size());
        textView5.setText(correctAnswer + " / " + all);
        buttonNewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to start a new quiz
                Global.answersModelList.clear();
                Global.quizList.clear();
                Intent intent = new Intent(FinishQuiz.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.answersModelList.clear();
                Global.quizList.clear();
                Global.Name = "";
                Intent intent = new Intent(FinishQuiz.this, StartQuiz.class);
                startActivity(intent);
            }
        });
    }
}




//Global.java

package com.example.quizapp;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static List<AnswersModel> answersModelList = new ArrayList<>();
    public static String Name ="";
    public static List<QuizModel> quizList = new ArrayList<>();
}


//QuizAdapter.java


package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ItemViewHolder> {
    private Context context;
    private List<QuizModel> quizModellist;
    public QuizAdapter(Context context, List<QuizModel> quizModellist) {
        this.context = context;
        this.quizModellist = quizModellist;
    }
    @NonNull
    @Override
    public QuizAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.ItemViewHolder holder, int position) {
        QuizModel quizModel = quizModellist.get(position);
        holder.question.setText(String.valueOf(position+1)+") "+quizModel.getQuestion());
        holder.radioButton.setText(quizModel.getAnswers().get(0));
        holder.radioButton2.setText(quizModel.getAnswers().get(1));
        holder.radioButton3.setText(quizModel.getAnswers().get(2));
        holder.radioButton4.setText(quizModel.getAnswers().get(3));

        // Reset text color for all radio buttons
        holder.radioButton.setTextColor(context.getResources().getColor(android.R.color.black));
        holder.radioButton2.setTextColor(context.getResources().getColor(android.R.color.black));
        holder.radioButton3.setTextColor(context.getResources().getColor(android.R.color.black));
        holder.radioButton4.setTextColor(context.getResources().getColor(android.R.color.black));


        // Temporarily remove the listener
        holder.radioGroup.setOnCheckedChangeListener(null);
        // Reset the radio group to clear previous selections
        holder.radioGroup.clearCheck();


        AnswersModel savedAnswer = findAnswerByQuestion(position + 1);
        if (savedAnswer != null) {
            int savedAnswerIndex = savedAnswer.getAnswer() - 1; // Assuming answer indices start at 1
            switch (savedAnswerIndex) {
                case 0:
                    holder.radioButton.setChecked(true);
                    break;
                case 1:
                    holder.radioButton2.setChecked(true);
                    break;
                case 2:
                    holder.radioButton3.setChecked(true);
                    break;
                case 3:
                    holder.radioButton4.setChecked(true);
                    break;
                default:
                    break; // Handle unexpected index
            }
        }

        // Set up a listener to detect when any radio button is checked
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // You can use checkedId to determine which radio button was clicked
                int radioButtonIndex = -1; // Default or error value
                switch (checkedId) {
                    case R.id.radioButton:
                        radioButtonIndex = 0;
                        break;
                    case R.id.radioButton2:
                        radioButtonIndex = 1;
                        break;
                    case R.id.radioButton3:
                        radioButtonIndex = 2;
                        break;
                    case R.id.radioButton4:
                        radioButtonIndex = 3;
                        break;
                }
                boolean check = false;
                AnswersModel itemc = null;
                if(Global.answersModelList.size() > 0 ){
                    for (AnswersModel item:Global.answersModelList ) {
                        if(item.getQuestion() == position + 1){
                            check = true;
                            itemc = item;
                        }
                       
                    }
                    if(radioButtonIndex != -1) {
                        if (check) {
                            Global.answersModelList.remove(itemc);
                            AddItem(position + 1, radioButtonIndex + 1);

                        } else {
                            AddItem(position + 1, radioButtonIndex + 1);
                        }
                    }
                }
                else {
                    AddItem(position+1,radioButtonIndex+1 );
                }
                // Reset text color of all radio buttons
                holder.radioButton.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.radioButton2.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.radioButton3.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.radioButton4.setTextColor(ContextCompat.getColor(context, android.R.color.black));

                // Check if the selected answer is correct or not
                int correctAnswerIndex = Global.quizList.get(position).getAnswer();
                if (correctAnswerIndex != radioButtonIndex+1) {
                    // The selected answer is wrong, so change the color of the correct answer
                    switch (correctAnswerIndex) {
                        case 1:
                            holder.radioButton.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 2:
                            holder.radioButton2.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 3:
                            holder.radioButton3.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 4:
                            holder.radioButton4.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private AnswersModel findAnswerByQuestion(int questionNumber) {
        for (AnswersModel item : Global.answersModelList) {
            if (item.getQuestion() == questionNumber) {
                return item;
            }
        }
        return null; //
    }
    private void AddItem(int i, int i1) {
        if(i1 != 0) {
            AnswersModel answersModel = new AnswersModel();
            answersModel.setAnswer(i1);
            answersModel.setQuestion(i);

            Global.answersModelList.add(answersModel);
        }

    }

    @Override
    public int getItemCount() {
        return quizModellist.size();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView question;
        private RadioGroup radioGroup;
        private  RadioButton radioButton, radioButton2, radioButton3, radioButton4;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            question = itemView.findViewById(R.id.textView);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButton2 = itemView.findViewById(R.id.radioButton2);
            radioButton3 = itemView.findViewById(R.id.radioButton3);
            radioButton4 = itemView.findViewById(R.id.radioButton4);
        }
    }
}



//QuizModel.java


package com.example.quizapp;

import java.util.ArrayList;
import java.util.List;

public class QuizModel {
    String question;
    List<String> answers = new ArrayList<>();
    int answer;

    public QuizModel(String question, List<String> answers, int answer) {
        this.question = question;
        this.answers = answers;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

}



//StartQuiz.java

package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartQuiz extends AppCompatActivity {
    private EditText nameEditText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        // Initialize the EditText and Button by finding them by ID
        nameEditText = findViewById(R.id.editTextTextPersonName);
        startButton = findViewById(R.id.button);

        // Set an OnClickListener on the button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String name = nameEditText.getText().toString();
                if(!name.trim().isEmpty()){
                    Global.Name = name;
                    Intent intent = new Intent(StartQuiz.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(StartQuiz.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
