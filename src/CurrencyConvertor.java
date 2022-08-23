import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;
public class CurrencyConvertor {
    public static void main(String[] args) throws IOException {
        Boolean isWorking = true;
        do {
            HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();

            currencyCodes.put(1, "USD");
            currencyCodes.put(2, "CAD");
            currencyCodes.put(3, "EUR");
            currencyCodes.put(4, "HKD");
            currencyCodes.put(5, "INR");

            Integer fromIndex, toIndex;
            String fromCode, toCode;
            double amount;

            Scanner sc = new Scanner(System.in);

            System.out.println("Welcome to currency convertor !\nCurrency converting from:");
            for (int i = 1; i <= currencyCodes.size(); i++) {
                System.out.print(i + ":" + currencyCodes.get(i) + "\t");
            }
            System.out.println("\n");
            fromIndex = sc.nextInt();
            while (fromIndex < 1 || fromIndex > 5) {
                System.out.println("Please, enter the valid currency code (1-5)");
                for (int i = 1; i <= currencyCodes.size(); i++) {
                    System.out.print(i + ":" + currencyCodes.get(i) + "\t");
                }
                System.out.println("\n");
                fromIndex = sc.nextInt();
            }
            fromCode = currencyCodes.get(fromIndex);

            System.out.println("Currency converting to:");
            for (int i = 1; i <= currencyCodes.size(); i++) {
                System.out.print(i + ":" + currencyCodes.get(i) + "\t");
            }
            System.out.println("\n");
            toIndex = sc.nextInt();
            while (toIndex < 1 || toIndex > 5) {
                System.out.println("Please, enter the valid currency code (1-5)");
                for (int i = 1; i <= currencyCodes.size(); i++) {
                    System.out.print(i + ":" + currencyCodes.get(i) + "\t");
                }
                System.out.println("\n");
                toIndex = sc.nextInt();
            }
            toCode = currencyCodes.get(toIndex);

            System.out.println("Amount that you wish to convert ?");
            amount = sc.nextFloat();

            sendGetRequest(fromCode, toCode, amount);

            System.out.println("Do you want to continue ? Type 1:Yes or other integer:No");
            if (sc.nextInt() != 1){
                isWorking = false;
            }
        }while (isWorking);
        System.out.println("Thank you for using my convertor !");
    }

    public static void sendGetRequest(String from, String to, Double amount) throws IOException {

        DecimalFormat f = new DecimalFormat("00.00");

        String GET_URL = "https://api.apilayer.com/exchangerates_data/latest?symbols="+ from +"&base="+ to;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("apikey", "");
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

            JSONObject obj = new JSONObject(response.toString());
            Double exchangeRate = obj.getJSONObject("rates").getDouble(from);
            System.out.format("%s/%s:%.3f\n",from,to,exchangeRate);
            double result = amount/exchangeRate;
            System.out.format("%.2f%s=%.2f%s \n",amount,from,result,to);
        }else {
            System.out.println("Oops, something goes wrong...");
        }
    }
}
