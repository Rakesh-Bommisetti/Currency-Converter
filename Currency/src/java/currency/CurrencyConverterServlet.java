package currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rakesh
 */
@WebServlet(name = "CurrencyConverterServlet", urlPatterns = {"/CurrencyConverterServlet"})
public class CurrencyConverterServlet extends HttpServlet {
    
    private static final String API_KEY = ""; // Place your API key here.
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/"+API_KEY+"/latest/";
    private double rates;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String fromCurrency = request.getParameter("fromCurrency");
        String toCurrency = request.getParameter("toCurrency");
        double amount = Double.parseDouble(request.getParameter("amount"));
        rates = convertCurrency(fromCurrency, toCurrency, amount);
        out.println("<html><head><title>Currency Conversion Result</title></head><body>");
        out.println("<h2>Currency Conversion Result</h2>");
        out.println("<p>Amount: " + amount + " " + fromCurrency + "</p>");
        out.println("<p>Converted Amount: " + rates + " " + toCurrency + "</p>");
        out.println("</body></html>");
        
    }

    private double convertCurrency(String fromCurrency, String toCurrency, double amount) throws IOException {
        URL url = new URL(API_URL + fromCurrency);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            scanner.close();

            // Parse the JSON response to get the conversion rate
            double conversionRate = Double.parseDouble(inline.substring(inline.indexOf(toCurrency + "\":") + 5, inline.indexOf(",", inline.indexOf(toCurrency + "\":"))));

            return amount * conversionRate;
        }

        return 0.0;
    }

}
