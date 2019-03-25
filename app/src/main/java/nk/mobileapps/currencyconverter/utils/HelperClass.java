package nk.mobileapps.currencyconverter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperClass {

    //public static final String urlstr = "http://www.google.com/finance/converter?a=1&from=%s&to=%s";
    //http://data.fixer.io/api/latest?access_key=a7089eae57c587163218926f7d7990b1
    //http://data.fixer.io/api/latest?access_key=a7089eae57c587163218926f7d7990b1&symbols=USD,AUD,CAD,PLN,MXN&format=1
    //http://data.fixer.io/api/2013-03-16?access_key=a7089eae57c587163218926f7d7990b1&symbols=USD,AUD,CAD,PLN,MXN&format=1
    //public static final String urlstr = "http://data.fixer.io/api/convert?access_key=a7089eae57c587163218926f7d7990b1&from=%s&to=%s&amount=1";
    public static final String urlstr = "http://data.fixer.io/api/lastest?access_key=a7089eae57c587163218926f7d7990b1&symbols=%s,%s&format=1";
    public static final Pattern pattern = Pattern
            .compile("<div id=currency_converter_result>1 [A-Z]{3} = <span class=bld>([0-9\\.]+) [A-Z]{3}</span>");
    public static String currency_data[][] = {
            {"AED", "United Arab Emirates Dirham (AED)"},
            {"AFN", "Afghan Afghani (AFN)"}, {"ALL", "Albanian Lek (ALL)"},
            {"AMD", "Armenian Dram (AMD)"},
            {"ANG", "Netherlands Antillean Guilder (ANG)"},
            {"AOA", "Angolan Kwanza (AOA)"},
            {"ARS", "Argentine Peso (ARS)"},
            {"AUD", "Australian Dollar (A$)"},
            {"AWG", "Aruban Florin (AWG)"},
            {"AZN", "Azerbaijani Manat (AZN)"},
            {"BAM", "Bosnia-Herzegovina Convertible Mark (BAM)"},
            {"BBD", "Barbadian Dollar (BBD)"},
            {"BDT", "Bangladeshi Taka (BDT)"},
            {"BGN", "Bulgarian Lev (BGN)"},
            {"BHD", "Bahraini Dinar (BHD)"},
            {"BIF", "Burundian Franc (BIF)"},
            {"BMD", "Bermudan Dollar (BMD)"},
            {"BND", "Brunei Dollar (BND)"},
            {"BOB", "Bolivian Boliviano (BOB)"},
            {"BRL", "Brazilian Real (R$)"},
            {"BSD", "Bahamian Dollar (BSD)"}, {"BTC", "Bitcoin (฿)"},
            {"BTN", "Bhutanese Ngultrum (BTN)"},
            {"BWP", "Botswanan Pula (BWP)"},
            {"BYR", "Belarusian Ruble (BYR)"},
            {"BZD", "Belize Dollar (BZD)"},
            {"CAD", "Canadian Dollar (CA$)"},
            {"CDF", "Congolese Franc (CDF)"}, {"CHF", "Swiss Franc (CHF)"},
            {"CLF", "Chilean Unit of Account (UF) (CLF)"},
            {"CLP", "Chilean Peso (CLP)"}, {"CNH", "CNH (CNH)"},
            {"CNY", "Chinese Yuan (CN¥)"}, {"COP", "Colombian Peso (COP)"},
            {"CRC", "Costa Rican Colón (CRC)"},
            {"CUP", "Cuban Peso (CUP)"},
            {"CVE", "Cape Verdean Escudo (CVE)"},
            {"CZK", "Czech Republic Koruna (CZK)"},
            {"DEM", "German Mark (DEM)"},
            {"DJF", "Djiboutian Franc (DJF)"},
            {"DKK", "Danish Krone (DKK)"}, {"DOP", "Dominican Peso (DOP)"},
            {"DZD", "Algerian Dinar (DZD)"},
            {"EGP", "Egyptian Pound (EGP)"},
            {"ERN", "Eritrean Nakfa (ERN)"},
            {"ETB", "Ethiopian Birr (ETB)"}, {"EUR", "Euro (€)"},
            {"FIM", "Finnish Markka (FIM)"},
            {"FJD", "Fijian Dollar (FJD)"},
            {"FKP", "Falkland Islands Pound (FKP)"},
            {"FRF", "French Franc (FRF)"},
            {"GBP", "British Pound Sterling (£)"},
            {"GEL", "Georgian Lari (GEL)"}, {"GHS", "Ghanaian Cedi (GHS)"},
            {"GIP", "Gibraltar Pound (GIP)"},
            {"GMD", "Gambian Dalasi (GMD)"},
            {"GNF", "Guinean Franc (GNF)"},
            {"GTQ", "Guatemalan Quetzal (GTQ)"},
            {"GYD", "Guyanaese Dollar (GYD)"},
            {"HKD", "Hong Kong Dollar (HK$)"},
            {"HNL", "Honduran Lempira (HNL)"},
            {"HRK", "Croatian Kuna (HRK)"},
            {"HTG", "Haitian Gourde (HTG)"},
            {"HUF", "Hungarian Forint (HUF)"},
            {"IDR", "Indonesian Rupiah (IDR)"},
            {"IEP", "Irish Pound (IEP)"},
            {"ILS", "Israeli New Sheqel (₪)"},
            {"INR", "Indian Rupee (Rs.)"}, {"IQD", "Iraqi Dinar (IQD)"},
            {"IRR", "Iranian Rial (IRR)"},
            {"ISK", "Icelandic Króna (ISK)"},
            {"ITL", "Italian Lira (ITL)"},
            {"JMD", "Jamaican Dollar (JMD)"},
            {"JOD", "Jordanian Dinar (JOD)"}, {"JPY", "Japanese Yen (¥)"},
            {"KES", "Kenyan Shilling (KES)"},
            {"KGS", "Kyrgystani Som (KGS)"},
            {"KHR", "Cambodian Riel (KHR)"},
            {"KMF", "Comorian Franc (KMF)"},
            {"KPW", "North Korean Won (KPW)"},
            {"KRW", "South Korean Won (₩)"},
            {"KWD", "Kuwaiti Dinar (KWD)"},
            {"KYD", "Cayman Islands Dollar (KYD)"},
            {"KZT", "Kazakhstani Tenge (KZT)"},
            {"LAK", "Laotian Kip (LAK)"}, {"LBP", "Lebanese Pound (LBP)"},
            {"LKR", "Sri Lankan Rupee (LKR)"},
            {"LRD", "Liberian Dollar (LRD)"},
            {"LSL", "Lesotho Loti (LSL)"},
            {"LTL", "Lithuanian Litas (LTL)"},
            {"LVL", "Latvian Lats (LVL)"}, {"LYD", "Libyan Dinar (LYD)"},
            {"MAD", "Moroccan Dirham (MAD)"},
            {"MDL", "Moldovan Leu (MDL)"},
            {"MGA", "Malagasy Ariary (MGA)"},
            {"MKD", "Macedonian Denar (MKD)"},
            {"MMK", "Myanmar Kyat (MMK)"},
            {"MNT", "Mongolian Tugrik (MNT)"},
            {"MOP", "Macanese Pataca (MOP)"},
            {"MRO", "Mauritanian Ouguiya (MRO)"},
            {"MUR", "Mauritian Rupee (MUR)"},
            {"MVR", "Maldivian Rufiyaa (MVR)"},
            {"MWK", "Malawian Kwacha (MWK)"},
            {"MXN", "Mexican Peso (MX$)"},
            {"MYR", "Malaysian Ringgit (MYR)"},
            {"MZN", "Mozambican Metical (MZN)"},
            {"NAD", "Namibian Dollar (NAD)"},
            {"NGN", "Nigerian Naira (NGN)"},
            {"NIO", "Nicaraguan Córdoba (NIO)"},
            {"NOK", "Norwegian Krone (NOK)"},
            {"NPR", "Nepalese Rupee (NPR)"},
            {"NZD", "New Zealand Dollar (NZ$)"},
            {"OMR", "Omani Rial (OMR)"},
            {"PAB", "Panamanian Balboa (PAB)"},
            {"PEN", "Peruvian Nuevo Sol (PEN)"},
            {"PGK", "Papua New Guinean Kina (PGK)"},
            {"PHP", "Philippine Peso (Php)"}, {"PKG", "PKG (PKG)"},
            {"PKR", "Pakistani Rupee (PKR)"},
            {"PLN", "Polish Zloty (PLN)"},
            {"PYG", "Paraguayan Guarani (PYG)"},
            {"QAR", "Qatari Rial (QAR)"}, {"RON", "Romanian Leu (RON)"},
            {"RSD", "Serbian Dinar (RSD)"}, {"RUB", "Russian Ruble (RUB)"},
            {"RWF", "Rwandan Franc (RWF)"}, {"SAR", "Saudi Riyal (SAR)"},
            {"SBD", "Solomon Islands Dollar (SBD)"},
            {"SCR", "Seychellois Rupee (SCR)"},
            {"SDG", "Sudanese Pound (SDG)"},
            {"SEK", "Swedish Krona (SEK)"},
            {"SGD", "Singapore Dollar (SGD)"},
            {"SHP", "St. Helena Pound (SHP)"},
            {"SLL", "Sierra Leonean Leone (SLL)"},
            {"SOS", "Somali Shilling (SOS)"},
            {"SRD", "Surinamese Dollar (SRD)"},
            {"STD", "São Tomé &amp; Príncipe Dobra (STD)"},
            {"SVC", "Salvadoran Colón (SVC)"},
            {"SYP", "Syrian Pound (SYP)"},
            {"SZL", "Swazi Lilangeni (SZL)"}, {"THB", "Thai Baht (THB)"},
            {"TJS", "Tajikistani Somoni (TJS)"},
            {"TMT", "Turkmenistani Manat (TMT)"},
            {"TND", "Tunisian Dinar (TND)"},
            {"TOP", "Tongan Paʻanga (TOP)"}, {"TRY", "Turkish Lira (TRY)"},
            {"TTD", "Trinidad &amp; Tobago Dollar (TTD)"},
            {"TWD", "New Taiwan Dollar (NT$)"},
            {"TZS", "Tanzanian Shilling (TZS)"},
            {"UAH", "Ukrainian Hryvnia (UAH)"},
            {"UGX", "Ugandan Shilling (UGX)"}, {"USD", "US Dollar ($)"},
            {"UYU", "Uruguayan Peso (UYU)"},
            {"UZS", "Uzbekistan Som (UZS)"},
            {"VEF", "Venezuelan Bolívar (VEF)"},
            {"VND", "Vietnamese Dong (₫)"}, {"VUV", "Vanuatu Vatu (VUV)"},
            {"WST", "Samoan Tala (WST)"}, {"XAF", "CFA Franc BEAC (FCFA)"},
            {"XCD", "East Caribbean Dollar (EC$)"},
            {"XDR", "Special Drawing Rights (XDR)"},
            {"XOF", "CFA Franc BCEAO (CFA)"}, {"XPF", "CFP Franc (CFPF)"},
            {"YER", "Yemeni Rial (YER)"},
            {"ZAR", "South African Rand (ZAR)"},
            {"ZMK", "Zambian Kwacha (1968–2012) (ZMK)"},
            {"ZMW", "Zambian Kwacha (ZMW)"},
            {"ZWL", "Zimbabwean Dollar (2009) (ZWL)"},};

    public static void putSharedPreferencesString(Context context, String key,
                                                  String val) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.putString(key, val);
        edit.commit();
    }

    public static String getSharedPreferencesString(Context context,
                                                    String key, String _default) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getString(key, _default);
    }

    public synchronized static double getCurrencyValue(Context c,
                                                       String fromCurrencyCode, String toCurrencyCode)
            throws RuntimeException {
        /*http://data.fixer.io/api/latest?access_key=a7089eae57c587163218926f7d7990b1*/
        /*http://data.fixer.io/api/convert

    ? access_key = YOUR_ACCESS_KEY
    & from = USD
    & to = EUR
    & amount = 25*/

        try {
            String url = String
                    .format(urlstr, fromCurrencyCode, toCurrencyCode);

            HttpGet httpget = new HttpGet(url);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent(),
                                EntityUtils.getContentCharSet(entity)));
                String line = null;
                Matcher matcher = null;
                while ((line = reader.readLine()) != null) {
                    matcher = matcher == null ? pattern.matcher(line) : matcher
                            .reset(line);
                    if (matcher.matches()) {
                        httpget.abort();
                        return Double.parseDouble(matcher.group(1));
                    }
                }
                httpget.abort();
                throw new Exception("Could not find [" + pattern.toString()
                        + "] in response to [" + url + "]");

            }
            if (entity != null) {
                entity.consumeContent();
            }
            throw new Exception("Got [" + statusCode + "] in response to ["
                    + url + "]");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get conversion rate from "
                    + fromCurrencyCode + " to " + toCurrencyCode, e);
        }
    }


}
