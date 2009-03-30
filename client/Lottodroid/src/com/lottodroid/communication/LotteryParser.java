package com.lottodroid.communication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lottodroid.model.Bonoloto;
import com.lottodroid.model.Lottery;
import com.lottodroid.model.Quiniela;

/**
 * Parser for the all the lottery draws ( bonoloto, quiniela, etc. ) retrieved
 * from the server. 
 * 
 * Convert the information into {@link Lottery} value objects
 */
class LotteryParser {
  private static final DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");

  private LotteryParser() {
    // Instantiation is not allowed for this class
  }

  /**
   * Parse the string containing the information of one or more bonolotos and
   * converts them into a list of {@link Bonoloto} value objects
   * 
   * @param response String that represents the response from the server to a lottery request
   * @return List<Lottery> A list of lottery objects representing the data
   * @throws JSONException
   * @throws ParseException
   */
  public static List<Bonoloto> parseBonoloto(String response) throws LotteryParseException {
    try {
      return parseBonolotoData(parseContentTypeAndGetData(response, "bonoloto"));

    } catch (JSONException e) {
      throw new LotteryParseException("Error parsing bonoloto content", e);
    } catch (ParseException ex) {
      throw new LotteryParseException("Error parsing bonoloto date", ex);
    }
  }

  /**
   * Analogous to {@link #parseBonoloto}
   */
  public static List<Quiniela> parseQuiniela(String response) throws LotteryParseException {
    try {
      return parseQuinielaData(parseContentTypeAndGetData(response, "quiniela"));

    } catch (JSONException e) {
      throw new LotteryParseException("Error parsing quiniela content", e);
    } catch (ParseException ex) {
      throw new LotteryParseException("Error parsing quiniela date", ex);
    }
  }

  /**
   * Parse the string containing the information of the last results of
   * every lottery draw and converts them into a list of {@link Lottery} 
   * value objects
   * 
   * @param response String that represents the response from the server to a lottery request
   * @return List<Lottery> A list of lottery objects representing the data
   * @throws JSONException
   * @throws ParseException
   */
  public static List<Lottery> parseAllLotteries(String response) throws LotteryParseException {
    try {
      String jsonData = parseContentTypeAndGetData(response, "sorteos");
      JSONObject jsonObject = new JSONObject(jsonData);

      List<Lottery> listLottery = new LinkedList<Lottery>();

      List<Bonoloto> listBonoloto = parseBonolotoData(jsonObject.getString("bonoloto"));
      // List<Quiniela> listQuiniela = parseQuinielaData(jsonObject.getString("quiniela"));

      listLottery.add(listBonoloto.get(0));
      //listLottery.add(new Quiniela());

      return listLottery;

    } catch (JSONException e) {
      throw new LotteryParseException("Error parsing lottery content", e);
    } catch (ParseException ex) {
      throw new LotteryParseException("Error parsing lottery date", ex);
    }
  }

  /**
   * Parse the string containing the information of a lottery draw checking that the data retrieved
   * is the same type as the one requested
   * 
   * @param response Represents the response from the server to a lottery request
   * @param contentType Lottery type requested
   * @return Data retrieved for a lottery draw
   * @throws LotteryParseException
   * @throws JSONException
   */
  private static String parseContentTypeAndGetData(String response, String contentType)
      throws LotteryParseException, JSONException {
    JSONObject jsonObject = new JSONObject(response);

    String jsonContentType = jsonObject.getString("info");
    String strData = jsonObject.getString("data");

    if (jsonContentType.equals(contentType)) {
      return strData;
    } else {
      if (jsonContentType.equals("error"))
        throw new LotteryParseException(strData);
      else
        throw new LotteryParseException("Wrong controller selected");
    }
  }

  private static List<Bonoloto> parseBonolotoData(String strContent) throws JSONException, ParseException {
    List<Bonoloto> lotteryList = new LinkedList<Bonoloto>();

    JSONArray jsonContent = new JSONArray(strContent);
    int numItems = jsonContent.length();

    for (int i = 0; i < numItems; i++) {
      JSONObject item = jsonContent.getJSONObject(i);

      String numbers = item.getString("num1") + " " + item.getString("num2") + " "
          + item.getString("num3") + " " + item.getString("num4") + " " + item.getString("num5") + " "
          + item.getString("num6");

      Date date = dfm.parse(item.getString("fecha"));
      String reintegro = item.getString("reintegro");
      String complementario = item.getString("reintegro");

      // Create a new Bonoloto object
      lotteryList.add(new Bonoloto(date, numbers, reintegro, complementario));
    }

    return lotteryList;
  }

  private static List<Quiniela> parseQuinielaData(String strContent) throws JSONException, ParseException {
    List<Quiniela> lotteryList = new LinkedList<Quiniela>();

    JSONArray jsonContent = new JSONArray(strContent);
    int numItems = jsonContent.length();

    for (int i = 0; i < numItems; i++) {
      JSONObject item = jsonContent.getJSONObject(i);
      //Date date = dfm.parse(item.getString("fecha"));

      JSONArray results = item.getJSONArray("results");
      int numResults = results.length();

      for (int j = 0; j < numResults; j++) {
        ;
        //JSONObject match = results.getJSONObject(i);

       // String local = match.getString("local");
       // String visitant = match.getString("visitante");
       // String result = match.getString("resultado");
      }
    }

    return lotteryList;
  }

}
