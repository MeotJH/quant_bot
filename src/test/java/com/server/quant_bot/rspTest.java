package com.server.quant_bot;

import java.util.Random;
import java.util.Scanner;

public class rspTest {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int win = 0;
        int lose = 0;
        int tie = 0;
        while(true){

            System.out.println("Please choose (r)ock, (p)aper or (s)cissors:");
            String answer = input.nextLine();

            if(answer.equals("r")) {
                String[] str = { "You chose rock. I chose paper. You lose!", /* lose++; */ "You chose rock. I chose scissors. You win!",  /* win++; */ "You chose rock. I chose rock. We tie!"  /* tie++; */ };
                Random r = new Random();
                int i = r.nextInt(3);

                if( i == 0 ) ++lose;
                if( i == 1 ) ++win;
                if( i == 2 ) ++tie;
                System.out.println(str[i]);



            }
            if(answer.equals("p")) {
                String[] str = { "You chose paper. I chose scissors. You lose!",  /* lose++; */ "You chose paper. I chose rock. You win!",  /* win++; */ "You chose paper. I chose paper. We tie!"  /* tie++; */ };
                Random r = new Random();
                int i = r.nextInt(3);

                if( i == 0 ) ++lose;
                if( i == 1 ) ++win;
                if( i == 2 ) ++tie;
                System.out.println(str[i]);

            }
            if(answer.equals("s")) {
                String[] str = { "You chose scissors. I chose rock. You lose!",  /* lose++; */ "You chose scissors. I chose paper. You win!",  /* win++; */ "You chose scissors. I chose scissors. We tie!"  /* tie++; */ };
                Random r = new Random();
                int i = r.nextInt(3);

                if( i == 0 ) ++lose;
                if( i == 1 ) ++win;
                if( i == 2 ) ++tie;
                System.out.println(str[i]);

            }


            System.out.println("Score: " + win + " win(s), " + lose + " loss(es), " + tie + " tie(s)");
            System.out.println("Play again (y/n)?");
            String c = input.nextLine();

            if(c.equalsIgnoreCase("n")){
                break;
            }

        }

    }





}
