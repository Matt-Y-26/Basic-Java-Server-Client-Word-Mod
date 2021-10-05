package com.example.halper.qaclient;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CommunicateActivity extends AppCompatActivity {

   private static Socket socket = null;
   private static PrintWriter out = null;
   private static Scanner in = null;
   private static TextView tv;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_communicate);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
           }
       });

       int port;
       String hostname;

       // Get the hostname from the intent

       Intent intent = getIntent();
       hostname = intent.getStringExtra(MainActivity.HOST_NAME);

       // Get the port from the intent.  Default port is 4000

       port = intent.getIntExtra(MainActivity.PORT, 4000);

       // get a handle on the TextView for displaying the status

       tv = (TextView) findViewById(R.id.text_answer);

       // Try to open the connection to the server

       try
       {
           socket = new Socket(hostname, port);

           out = new PrintWriter(socket.getOutputStream(), true);
           in = new Scanner(new InputStreamReader(socket.getInputStream()));

           //tv to set up instructions to client
           tv.setText("Connected to server. ");
           tv.append("Your options are:"+"\n");
           tv.append("cap : capitalize first letter of every word"+"\n");
           tv.append("longestword : Find longest word that does contain e"+"\n");
           tv.append("wordcount47 : Finds words 4 or more and 7 or less words long"+"\n");
           tv.append("bigram : creates a bigram frequency chart"+"\n");
           tv.append("vowelcount : vowel count, creates a small frequency for every vowel"+"\n");
       }
       catch (IOException e)  // socket problems
       {
           tv.setText("Problem: " + e.toString());
           socket = null;
       }

   } // end onCreate

   public void sendQuestion(View view)
           throws java.io.IOException
   {
       EditText et1;
       EditText et2;
       String user_question;
       String user_file;
       String answer;
       boolean finished = false;
       List<String> par = new LinkedList<String>();

       int num_lines=0;
       // are we connected?

       if(socket == null)
       {
           tv.setText("Not connected.");
       }

       else
       {
           // get the operation to send to the server (place it in "user_question")
           //get the number of lines and send it to the server aswell
           AssetManager assetManager = getAssets();
           et2 = (EditText)findViewById(R.id.edit_file);
           et1 = (EditText)findViewById(R.id.edit_question);
           user_question=et1.getText().toString();
           user_file=et2.getText().toString();
           Scanner scan = new Scanner(assetManager.open(user_file));



           // send number of lines to server
           while(scan.hasNext()) //scans how many lines there are
           {
               par.add(scan.nextLine());
               num_lines++;
           }
           out.println(user_question); //send operation (question)

           num_lines=par.size();
           out.println(num_lines); //send number of lines
           for(int x=0;x<num_lines;x++) //send sentences line by line
           {
               out.println(par.get(x));
           }

           int return_numlines = 0;
           String tempnum;
           tempnum=in.nextLine();
           return_numlines=Integer.parseInt(tempnum);
           tv.setText("Answer: ");
           tv.append("\n");
           //receives lines from client and prints them in the for loop
           for(int x=0;x<return_numlines;x++)
           {
               answer=in.nextLine(); //scan the answer
               tv.append(answer); //then append
               tv.append("\n");

           }


           // if we're finished, close the connection

           if(finished)
           {
               try
               {
                   out.close();
                   in.close();
                   socket.close();
                   socket = null;

                   tv.setText("Finished.  Connection closed.");
               }
               catch (IOException e)  // socket problems
               {
                   tv.setText("Problem: " + e.toString());
               }

           }

       }

   } // end sendQuestion

} // end CommunicateActivity
