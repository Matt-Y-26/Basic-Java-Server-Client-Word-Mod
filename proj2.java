    import java.util.*;
    import java.net.*;
    import java.io.*;


// This project is divided into two parts, the server which processes the data
// and the client which connects and sends the paragraph. They both interact with one
// another doing different operations including : longest word, capitalizing every
// first letter in every word, bigram frequency, and other operations.
//
// My custom option is vowel count, which takes the frequency of vowels in the
// paragraph given. With a large enough paragraph (and diverse) you could see how
// most of the time e is the most common vowel.



// The following shows the general form of a server that
// processes requests from different clients one-at-a-time.
// That is, it listens for a request from a client, processes
// that request, then looks for another client, and another,
// and another, etc.
//
// Note that the loop is an infinite loop ("while(true)"),
// so the program must be terminated manually (using Linux's
// CTRL-C command, for example).
//
// "..." below stands for omitted processing statements that
// would be resolved with specific processing statements,
// depending on the desired server behavior.

public class proj2
    {

     public static void main(String[] args)
         {

            ServerSocket serverSocket = null;
            Socket socket = null;
            int port;
            boolean listening = true; // assume serverSocket creation
                                      // was OK

            // get port # from command-line

               port = Integer.parseInt(args[0]);

            // try to create a server socket

            try
            {
                serverSocket = new ServerSocket(port);
            }
            catch(IOException e)
            {
                System.out.println(e);
                listening = false;
            }

       if(listening) // i.e., serverSocket successfully created
       {
         // continue to:
         //
         //   (1) Listen for a client request
         //   (2) Read data from the client
         //   (3) Process the request: do calculation and return value
         //

         while(true) // main processing loop
         {
                try
                 {

            // Listen for a connection request from a client

                socket = serverSocket.accept();

            // Establish the input and output streams on the socket

            PrintWriter out = new
                             PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(new
                           InputStreamReader(socket.getInputStream()));

            // Read data from the client, do calculation(s),
            // return data value(s)
            String operation;
            int num_lines;
            List<String> par = new LinkedList<String>();
            List<String> ans = new LinkedList<String>();
            String[] ansarr = new String[5];

            operation=in.nextLine();
            num_lines=in.nextInt();
            in.nextLine(); //moves pointer because nextInt does not
            for(int x= 0;x<num_lines;x++) //scan in paragraph
            {
                    par.add(in.nextLine());

            }

            //use switch to tell what operation is desired
            switch(operation) {
            case "cap": //capitalize every first letter of every word
                    ans=cap(par);
                    out.println(ans.size()); //sends the length of answer                
                    for(int q=0;q<ans.size();q++)
                    {
                            out.println(ans.get(q));
                    }
                    break;
            case "longestword": //find longest word and return it as well as length

                    String longestword =longestword(par);
                    out.println(2); //send number of lines
                    out.println(longestword);
                    out.println("Length is: "+longestword.length());
                    break;

            case "wordcount47": //return count of words that are 4 to 7 characters long inclusive
                    int total47;
                    total47=wordcount47(par);
                    out.println(1); //sending back 1 number so
                    out.println(total47);
                    break;

            case "bigram": //creates a bigram frequency chart
                    ansarr = bigram(par);
                    out.println(7); //send number of lines
                    out.println("Bigram Frequency");
                    out.println(""+ansarr[0]);
                    out.println(""+ansarr[1]);
                    out.println(""+ansarr[2]);
                    out.println(""+ansarr[3]);
                    out.println(""+ansarr[4]);
                    int total = 0;
                    int tempint = 0;
                    String tempintStr;
                    String temphold;
                   String[] temparr = new String[2]; //this is used for the seperation of value and key
                    for(int z =0;z<5;z++) //for loop goes through all indexes and counts them all up to give the total
                    {
                            temphold=ansarr[z];
                            temparr=temphold.split(" ");
                            tempintStr=temparr[1];
                            tempint=Integer.parseInt(tempintStr);
                            total=tempint+total;
                    }
                    out.println("Total "+total);
                    break;

            case "vowelcount": //vowel count, frequency of vowels
                    int[] temparrcustom = new int[5];
                    temparrcustom=customoption(par);
                    out.println(5); //sends how many lines are gonna be used
                    out.println("A"+" : "+temparrcustom[0]);
                    out.println("E"+" : "+temparrcustom[1]);
                    out.println("I"+" : "+temparrcustom[2]);
                    out.println("O"+" : "+temparrcustom[3]);
                    out.println("U"+" : "+temparrcustom[4]);
                    break;

            default: //if someone doesnt read of the direction or just wants to break programs
                    out.println(1); //number of lines sending back
                    out.println("You did not enter a valid option, please make sure there are no spaces and it all lower case");
            }

                // close connection to client

               out.close();
               in.close();
               socket.close();

            }


            catch(IOException e)
            {
               System.out.println(e);
            }

         } // end while (main processing loop)

       } // end if listening


     } // end main


     public static List<String> cap(List<String>par) //capitalize all first letters of every word
       {
             String holder;
             String[] holderarr = new String[256];
             List <String> answer = new LinkedList<String>();
             String firstletter;
             String word;
             String holder2 = "";


             for(int n=0; n<par.size(); n++) //goes through lines
             {
                     holder=par.get(n);
                     holderarr=holder.split(" ");

                     for(int x=0;x<holderarr.length;x++) //goes through words
                     {
                             firstletter = holderarr[x].substring(0, 1); //assigns firstletter as a separation of the word
                             firstletter=firstletter.toUpperCase();

                             word=holderarr[x].substring(1); //grabs everything but the first letter

                             holderarr[x]=firstletter+word; //putting that word back

                             }
                    for(int m=0;m<holderarr.length;m++) {

                            holder2=holder2+holderarr[m]+" "; //adds all the values together
                        
                    }
             answer.add(holder2);
             holder2=""; //resets holder2 so the next line added is not a combination of the previous line
             }

            return answer; //returns the answer


       }
       public static String longestword(List<String>par) //longest word
       {
               String holder;
               String clean;
               String[] holderarr = new String[256];
               String longestword= "";
               boolean success = true;

               for(int n=0; n<par.size(); n++) //goes through lines
                     {
                             holder=par.get(n);
                             clean=holder.replaceAll("[^a-zA-Z ]", ""); //removes the punc and stuff from string
                             holderarr=clean.split(" ");
                             for(int x=0;x<holderarr.length;x++) //goes through words
                             {
                                     if(holderarr[x].length()>longestword.length()) //checks to see if the current word beats the longest word                                       
                                     {

                                             for(int z = 0;holderarr[x].length()<z;z++) //goes through letters
                                             {
                                                     if(holderarr[x].charAt(z)==('e') || (holderarr[x].charAt(z)==('E')))
                                                     {
                                                             success=false;
                                                     }
                                             }
                                             if (success)
                                             {
                                                     longestword=holderarr[x];
                                             }

                                     }
                             }
                     }

            return longestword; //returns the word

       }
       public static int wordcount47(List<String>par) //number of words 4 to 7 inclusive
       {
               String holder;
               String clean;
               String[] holderarr = new String[256];
               int count = 0;

               for(int n=0; n<par.size(); n++) //goes through lines
                     {
                             holder=par.get(n);
                             clean=holder.replaceAll("[^a-zA-Z ]", ""); //removes the punc and stuff from string
                             holderarr=clean.split(" ");
                             for(int x=0;x<holderarr.length;x++) //goes through words
                             {
                                    int lengthstring=holderarr[x].length();
                                     if(holderarr[x].length()>=4 && holderarr[x].length()<=7) //if 4 to 7 inclusive...
                                     {
                                            count++; //...the count increments                                       
                                     }                                     
                             }
                     }

            return count; //returns the magic count number

       }
       public static String[] bigram(List<String>par)
       {
               String holder;
               String clean;
               String[] holderarr = new String[256];
               int count = 0;
               HashMap<String,Integer> bigrammap = new HashMap<String,Integer>();
                   

               for(int n=0; n<par.size(); n++) //goes through lines
                     {
                             holder=par.get(n);
                             clean=holder.replaceAll("[^a-zA-Z ]", ""); //removes the punc and stuff from string
                             holderarr=clean.split(" ");
                            for(int x=0;x<holderarr.length;x++) //goes through words
                             {
                                     for(int i=0;i<holderarr[x].length()-1;i++) //minus 1 because it would go over
                                     {     
                                             char c1 = holderarr[x].charAt(i);
                                             char c2 = holderarr[x].charAt(i+1);
                                             String cc= ""+c1+c2; //combines the chars                                             
                                             Integer value = bigrammap.get(cc);
                                             if(value != null) //checks if value doesnt exist
                                             {
                                                     bigrammap.put(cc,new Integer(value+1));

                                             }
                                             else //key doesnt exist so this adds thi key and its 1 value into the hashed map
                                             {
                                                     bigrammap.put(cc,1);
                                             }
                                             }

                                     }
                             }
               //now that I have a bigram with all my values in it I need to find the top 5
               String b1key="";
               String b2key="";
               String b3key="";
               String b4key="";
               String b5key="";
               int b1value = 0;
               int b2value = 0;
               int b3value = 0;
               int b4value = 0;
               int b5value = 0;
               String btempkey="";
               int btempvalue =0;               
               Iterator itr = bigrammap.entrySet().iterator();
                  
               while(itr.hasNext())
               {
                       Map.Entry keyvalue = (Map.Entry)itr.next();
                       btempkey=(String) keyvalue.getKey();
                       btempvalue=(int) keyvalue.getValue();
                      //disgustingly long if and else if statements that assign top 5 frequencies
                       if(btempvalue>b1value)
                       {
                               b5key=b4key; //have to reassign the whole thing if one of them (besides 5th) changes
                               b4key=b3key;
                               b3key=b2key;
                               b2key=b1key;
                               b1key=btempkey;
                               b5value=b4value;
                               b4value=b3value;
                               3value=b2value;
                               b2value=b1value;
                               b1value=btempvalue;
                       }
                       else if(btempvalue>b2value)
                       {
                               b5key=b4key;
                               b4key=b3key;
                               b3key=b2key;
                               b2key=btempkey;
                               b5value=b4value;
                               b4value=b3value;
                               b3value=b2value;
                               b2value=btempvalue;
                           
                       else if(btempvalue>b3value)
                      {
                              b5key=b4key;
                              b4key=b3key;
                              b3key=btempkey;
                              b5value=b4value;
                              b4value=b3value;
                              b3value=btempvalue;
                           
                       else if(btempvalue>b4value)
                       {
                               b5key=b4key;
                               b4key=btempkey;
                               b5value=b4value;
                               b4value=btempvalue;
                       }
                       else if(btempvalue>b5value)
                       {
                               b5value=btempvalue;
                               b5value=btempvalue;
                       }
                       //take keys and values and make 5 strings each string being a combination                         
               }
               String[] returnarr = new String [5];
                   
               String b1 = b1key +" "+ Integer.toString(b1value);
               String b2 = b2key +" "+ Integer.toString(b2value);
               String b3 = b3key +" "+ Integer.toString(b3value);
               String b4 = b4key +" "+ Integer.toString(b4value);
               String b5 = b5key +" "+ Integer.toString(b5value);

               returnarr[0]=b1;
               returnarr[1]=b2;
               returnarr[2]=b3;
               returnarr[3]=b4;
               returnarr[4]=b5;

               //returns an array with 5 strings each string being a combo of key and value so like "ab 3" or whatever
            return returnarr;

       }
       public static int[] customoption(List<String>par) //vowel count
       {
               String holder;
               String clean;
               String[] holderarr = new String[256];
               int acount=0;
               int ecount=0;
               int icount=0;
               int ocount=0;
               int ucount=0;

                   
               for(int n=0; n<par.size(); n++) //goes through lines
                     {
                             holder=par.get(n);
                             clean=holder.replaceAll("[^a-zA-Z ]", ""); //removes the punc and stuff from string
                             holderarr=clean.split(" ");
                             for(int x=0;x<holderarr.length;x++) //goes through words
                             {                                         
                                             for(int z = 0;holderarr[x].length()>z;z++) //goes through letters
                                             {
                                                         // if statements check for a vowel
                                                    if(holderarr[x].charAt(z)==('a') || (holderarr[x].charAt(z)==('A')))
                                                     {
                                                             acount++;
                                                     }
                                                     else if (holderarr[x].charAt(z)==('e') || (holderarr[x].charAt(z)==('E')))
                                                     {
                                                             ecount++;
                                                     }
                                                     else if (holderarr[x].charAt(z)==('i') || (holderarr[x].charAt(z)==('I')))
                                                     {
                                                             icount++;
                                                     }
                                                     else if (holderarr[x].charAt(z)==('o') || (holderarr[x].charAt(z)==('O')))
                                                     {
                                                             ocount++;
                                                     }
                                                     else if (holderarr[x].charAt(z)==('u') || (holderarr[x].charAt(z)==('U')))
                                                     {
                                                             ucount++;
                                                     }
                                             }
                             }
                     }
               int[] returnarr = new int[5];
               returnarr[0]=acount;
               returnarr[1]=ecount;
               returnarr[2]=icount;
               returnarr[3]=ocount;
               returnarr[4]=ucount;

               return returnarr;
       }
    } // end seqserver
