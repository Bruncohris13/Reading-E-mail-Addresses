import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;


public class EmailAddressFinder
{

    private final Logger logger = Logger.getLogger(EmailAddressFinder.class);

    private static ArrayList<String> emailAddresses;

    public static void main(String[] args)
    {
        emailAddresses = new ArrayList<String>();
        EmailAddressFinder eaf = new EmailAddressFinder();
        eaf.run();
        System.out.println("Email addresses found: " + emailAddresses.size());
    }

    public void run()
    {

        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader("corrupteddb"));

            String input = "";

            PrintWriter pw = new PrintWriter("eaf");

            while ((input = reader.readLine()) != null)
            {

                input = input.trim();

                ArrayList<String> temp = new ArrayList<String>();

                temp = findEmailAddresses(input);

                for (String t : temp)
                {
                    emailAddresses.add(t);
                }
            }

            pw.close();
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String> findEmailAddresses(String input) throws IOException
    {
        ArrayList<String> list = new ArrayList<>();

        ArrayDeque<String> noSpaces = new ArrayDeque<>(Arrays.asList(input.split(" ")));
        logger.debug("Size of noSpaces: " + noSpaces.size());

        while (!noSpaces.isEmpty())
        {
            String temp = noSpaces.removeFirst();
            logger.debug("temp: " + temp);

            while (temp.contains("@"))
            {
                int indexAt = temp.indexOf("@");

                String local = checkLocal(temp.substring(0, indexAt));
                logger.debug("local: " + local);

                if (local != null)
                {
                    String domain = checkDomain(temp.substring(indexAt + 1));
                    logger.debug("domain: " + domain);

                    if (domain != null)
                    {
                        String email = local + "@" + domain;
                        list.add(email);
                        temp = temp.substring(indexAt + 1 + domain.length());
                    } else
                    {
                        temp = temp.substring(indexAt + 1);
                    }
                } else
                {
                    temp = temp.substring(indexAt + 1);
                }
            }
        }


        //Writes the list to a file
        outputToFile(list);

        return list;
    }

    private String checkLocal(String local)
    {
        if (local.length() == 0)
            return null;

        char k = local.charAt(local.length() - 1);

        if (((k < 48) || (k > 57)) && ((k < 65) || (k > 90)) && ((k < 97) || (k > 122)) && (k != 95))
            return null;

        String correctLocal = "" + k;
        int i = 2;
        int periodCounter = 0;

        while ((correctLocal.length() < local.length()) && (periodCounter < 2))
        {
            char c = local.charAt(local.length() - i);

            if (((c < 48) || (c > 57)) && ((c < 65) || (c > 90)) && ((c < 97) || (c > 122)) && (c != 95) && (c != 46))
            {
                break;
            } else if (c == 46)
            {
                periodCounter++;
            }

            correctLocal = c + correctLocal;

            i++;
        }

        if (correctLocal.charAt(0) == 46)
            correctLocal = correctLocal.substring(1);

        return correctLocal;
    }

    private String checkDomain(String domain)
    {
        if (domain.length() == 0)
            return null;

        char k = domain.charAt(0);
        if (!Character.isLowerCase(k))
            return null;

        String correctDomain = "";
        int i = 0;
        int periodCounter = 0;
        boolean topDomainAdded = false;

        while ((i < domain.length()) && (periodCounter < 3))
        {
            char c = domain.charAt(i);

            if (!Character.isLowerCase(c) && (c != 46))
                return null;
            else if (c == 46)
            {
                periodCounter++;

                correctDomain += c;

                if (i + 3 < domain.length())
                {
                    String topDomain = "" + domain.charAt(i + 1) + domain.charAt(i + 2) + domain.charAt(i + 3);

                    if (topDomain.equals("net") || topDomain.equals("com"))
                    {
                        correctDomain += topDomain;
                        topDomainAdded = true;
                        break;
                    }
                }

                if (i + 2 < domain.length())
                {
                    String topDomain = "" + domain.charAt(i + 1) + domain.charAt(i + 2);

                    if (topDomain.equals("uk") || topDomain.equals("de") || topDomain.equals("jp") || topDomain.equals("ro"))
                    {
                        correctDomain += topDomain;
                        topDomainAdded = true;
                        break;
                    }
                }
            } else
            {
                correctDomain += c;
            }

            i++;
        }

        if ((periodCounter == 3) || (!topDomainAdded))
            return null;

        return correctDomain;
    }

    private void outputToFile(ArrayList<String> list) throws IOException
    {
        FileWriter writer = new FileWriter("output.txt");
        for (String str : list)
        {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }
}
